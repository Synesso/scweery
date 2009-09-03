package scweery

import java.sql.{Statement, DriverManager, Connection => JavaDBConnection, ResultSet, ResultSetMetaData}
import java.sql.Types._

object Connection {
  class Context(val c: Connection) {
    var stmt: Option[Statement] = None
    var rs: Option[ResultSet] = None

    def use(sql: String)(b: Row => Unit): Unit = make(sql)(b)

    def make[T](sql: String)(b: Row => T): List[T] = {
      executeQuery(sql)

      def namesFrom(md: ResultSetMetaData) = {
        def nextName(acc: List[String], i: Int): List[String] = if (i > 0) nextName(md.getColumnLabel(i) :: acc, i - 1) else acc
        nextName(Nil, md.getColumnCount)
      }
      val names: List[String] = rs.map(_.getMetaData).map(namesFrom(_)).getOrElse(Nil)

      def processRow(acc: List[T], rownum: Int): List[T] = {
        if (rs.map(_.next).getOrElse(false)) {
          def row = {
            def nextField(acc: List[Field], index: Int): List[Field] = {
              def field: Field = rs.get.getMetaData.getColumnType(index) match {
                case INTEGER => new IntField(names(index - 1), rs.get.getInt(index))
                case DOUBLE => new DoubleField(names(index - 1), rs.get.getDouble(index))
                case _ => new StringField(names(index - 1), rs.get.getString(index))
              }
              if (index > 0) nextField(field :: acc, index - 1) else acc
            }
            new Row(rownum, nextField(Nil, rs.get.getMetaData.getColumnCount))
          }
          val t = b(row)
          processRow(t :: acc, rownum + 1)
        } else acc
      }
      val ts = processRow(Nil, 0)
      close
      ts.reverse
    }

    private def executeQuery(sql: String) = {
      stmt = Some(c.connect.createStatement)
      rs = stmt.map(_.executeQuery(sql))
    }

    private def close = {
      rs.foreach(_.close)
      stmt.foreach(_.close)
    }
  }

  def using(c: Connection)(b: Context => Unit) = {
    val context = new Context(c)
    try {
      b(context)
    } catch {
      case e => e.printStackTrace
    } finally {
      c.disconnect
    }
  }
}

class Connection(val url: String, val user: String, val pass: String) {
  var jdbc: Option[JavaDBConnection] = None

  def connect: JavaDBConnection = {
    jdbc = Some(DriverManager.getConnection(url, user, pass))
    jdbc.get
  }

  def disconnect: Unit = {
    jdbc.foreach(_.close)
    jdbc = None
  }
}