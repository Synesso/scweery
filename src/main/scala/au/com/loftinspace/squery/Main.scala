package au.com.loftinspace.squery

import Connection._

object Main extends Application {
  val localMySQL = new Connection("jdbc:mysql://localhost/testing", "root", "root")

  using_1(localMySQL) { connection =>
    connection.query("select * from stuff") { l =>
      println(l(0) + "-->" + l(1))
    }
  }

//  val rs = using(localMySQL) query "select * from stuff"
//  while (rs.next) println(rs.getString(1) + "->"+ rs.getInt(2))
//  rs.close
}