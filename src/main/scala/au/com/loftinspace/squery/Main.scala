package au.com.loftinspace.squery

import Connection._
import runtime.RichString

object Main extends Application {
  val localMySQL = new Connection("jdbc:mysql://localhost/testing", "root", "root")
  val localPSQL = new Connection("jdbc:postgresql://localhost:5432/lab_development", "lab", "lab")

  using_1(localPSQL) { connection =>
    connection.query("select * from banned_phrases") { fields =>

            val r = fields(1).value.reverse
            println(fields(1) + " >><< " + r)

//      println("Reverse of name " + fields(1) +", row " + fields(0) + " is " + fields(1))
//      println("Reverse of name " + fields(1) +", row " + fields(0) + " is " + fields(1).reverse)
//      println("Class of field 1 = " + fields(0).getClass)
    }
  }
}



//            println(fields.map(String.valueOf(_)).mkString("\t"))
