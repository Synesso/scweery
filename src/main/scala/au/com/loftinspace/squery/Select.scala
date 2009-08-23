package au.com.loftinspace.squery


import java.sql.{Statement, DriverManager, Connection => JavaDBConnection, ResultSet}

//class Select1[+ T1](v1: T1) extends Tuple1[T1]
//class Select2[+ T1, + T2](v1: T1, v2: T2) extends Tuple1[T1,T2]
//class Select3[+ T1, + T2, + T3](v1: T1, v2: T2, v3: T3) extends Tuple1[T1,T2,T3]
//
//object Select {
//  def apply[A >: AnyRef](query: String): Seq[A] = {
//    new Select1("a")
//  }
//}
//
//class Results(rs: ResultSet)
//


object Connection {
 

  class Context123(val c: Connection) {
    var stmt: Option[Statement] = None
    var rs: Option[ResultSet] = None
    def query(sql: String)(b: List[String] => Unit) = {
      executeQuery(sql)
      while (rs.map(_.next).getOrElse(false)) {
        val l = rs.get.getString(1) :: rs.get.getInt(2).toString :: Nil
        b(l)
      }
      close
    }
    private def executeQuery(sql: String) = {
      stmt = Some(c.connect.createStatement)
      rs = stmt.map(_.executeQuery(sql))
    }
    private def close = {
      println("rs? " + rs.get.isClosed)
      rs.foreach(_.close)
      println("rs? " + rs.get.isClosed)
      println("stmt? " + stmt.get.isClosed)
      stmt.foreach(_.close)
      println("stmt? " + stmt.get.isClosed)
    }
  }

  def using_1(c: Connection)(b: Context123 => Unit) = {
    val context = new Context123(c)
    try {
      b(context)
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
    println("jdbc? " + jdbc.get.isClosed)
    jdbc.foreach(_.close)
    println("jdbc? " + jdbc.get.isClosed)
    jdbc = None
  }
}