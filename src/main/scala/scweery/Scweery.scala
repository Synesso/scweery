package scweery

import java.sql.ResultSetMetaData
import java.sql.Types._

object Scweery {

  /** Use a connection and return Unit. */
  def use(c: Connection)(b: ConnectionPhase => Unit) = infer[Unit](c)(b)

  /** Use a connection and make an object of type T. */
  def infer[T](c: Connection)(b: ConnectionPhase => T): T = {
    val phase = new ConnectionPhase(c)
    try {
      b(phase)
    } finally {
      phase.end
    }
  }

  class ConnectionPhase(c: Connection) {
    val jdbcConnection = c.connect

    def end = jdbcConnection.close

    /** Execute the given block once per row returned from exectuing the given SQL. The block returns Unit. */
    def use(sql: String)(block: Row => Unit) = inferListOf[Unit](sql)(block)

    /** Execute the given block once per row returned from exectuing the given SQL. The block returns a value of the given type T. */
    def inferListOf[T](sql: String)(block: Row => T): List[T] = {
      val statement = jdbcConnection.createStatement
      val resultSet = statement.executeQuery(sql)

      try {
        def namesFrom(md: ResultSetMetaData) = {
          def nextName(acc: List[String], i: Int): List[String] = if (i > 0) nextName(md.getColumnLabel(i) :: acc, i - 1) else acc
          nextName(Nil, md.getColumnCount)
        }
        val names: List[String] = namesFrom(resultSet.getMetaData)

        def processRow(acc: List[T], rownum: Int): List[T] = {
          if (resultSet.next) {
            def row = {
              def nextField(acc: List[Field], index: Int): List[Field] = {
                def field: Field = resultSet.getMetaData.getColumnType(index) match {
                  case INTEGER => new IntField(names(index - 1), resultSet.getInt(index))
                  case DOUBLE => new DoubleField(names(index - 1), resultSet.getDouble(index))
                  case TIMESTAMP => new DateField(names(index - 1), resultSet.getDate(index))
                  case _ => new StringField(names(index - 1), resultSet.getString(index))
                }
                if (index > 0) nextField(field :: acc, index - 1) else acc
              }
              new Row(rownum, nextField(Nil, resultSet.getMetaData.getColumnCount))
            }
            val t = block(row)
            processRow(t :: acc, rownum + 1)
          } else acc
        }
        processRow(Nil, 0).reverse
      }finally {
        resultSet.close
        statement.close
      }
    }
  }
}