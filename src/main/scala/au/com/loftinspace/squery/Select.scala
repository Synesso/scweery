package au.com.loftinspace.squery

import java.sql.{Statement, DriverManager, Connection => JavaDBConnection, ResultSet}

object Connection {
 

  class Context123(val c: Connection) {
    var stmt: Option[Statement] = None
    var rs: Option[ResultSet] = None
    def query(sql: String)(b: List[String] => Unit) = {
      executeQuery(sql)
      while (rs.map(_.next).getOrElse(false)) {
        def getString(acc: List[String], i: Int): List[String] = if (i > 0) getString(rs.get.getString(i) :: acc, i - 1) else acc
        val l = getString(Nil, rs.get.getMetaData.getColumnCount)
        b(l)
      }
      close
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

  def using_1(c: Connection)(b: Context123 => Unit) = {
    val context = new Context123(c)
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