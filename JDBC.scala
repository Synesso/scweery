import java.sql._
object Connection {
  Class.forName("com.mysql.jdbc.Driver")
  def connection[T >: Connection]: T = DriverManager.getConnection("jdbc:mysql://localhost/testing", "root", "root")
}

def describe(query: String): List[(String, java.lang.Class[_])] = {
  val c = Connection.connection
  val s = c.createStatement
  val rs = s.executeQuery(query)
  val md = rs.getMetaData
  val columnCount = md.getColumnCount
  def nextType(types: List[(String, java.lang.Class[_])], index: Int): List[(String, java.lang.Class[_])] = {
    if (index > columnCount) types else nextType((md.getColumnName(index), Class.forName(md.getColumnClassName(index))) :: types, index + 1)
  } 
  val description = nextType(Nil, 1).reverse
  rs.close
  s.close
  c.close
  description
}

describe("select * from stuff")

// todo - can the method return an iteratable?
// todo - can the type of row be derived?
def execute(query: String)(row: (String, Int) => Unit) = {
  val c = Connection.connection
  val s = c.createStatement
  val rs = s.executeQuery(query)
  val md = rs.getMetaData
  val columnCount = md.getColumnCount
  // todo - can the values for row be derived?
  while (rs.next) row(rs.getString(1), rs.getInt(2))
  rs.close
  s.close
  c.close
}

execute("select * from stuff") { (name, number) =>
  println(name+"'s number is "+number)
}

def executeAsList(query: String): List[_] = { // todo - return a tuple of varying length, not a list. must contain different types
  
}


/*
val query = "select name, number from stuff"
val c = Connection.connection
val s = c.createStatement
val rs = s.executeQuery(query)
val md = rs.getMetaData
md.getColumnCount
md.getColumnType(1) // see http://java.sun.com/j2se/1.4.2/docs/api/constant-values.html#java.sql.Types.ARRAY
rs.next
val (name, number) = (rs.getString(1), rs.getInt(2))
rs.close
s.close
c.close
*/
