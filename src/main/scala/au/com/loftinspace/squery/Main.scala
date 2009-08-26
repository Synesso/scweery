package au.com.loftinspace.squery

import Connection._

object Main extends Application {
  val localMySQL = new Connection("jdbc:mysql://localhost/testing", "root", "root")
  val localPSQL = new Connection("jdbc:postgresql://localhost:5432/lab_development", "lab", "lab")

  using_1(localPSQL) { connection =>
    connection.query("select * from banned_phrases") { l =>
            println(l.map(String.valueOf(_)).mkString("\t"))
    }
  }
}