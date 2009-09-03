package scweery

import Connection._

object Main extends Application {
  val localPSQL = new Connection("jdbc:postgresql://localhost:5432/lab_development", "lab", "lab")

  using(localPSQL) { connection =>
    connection.use("select * from banned_phrases") { row =>
      println("You are not allowed to say " + row.string("phrase"))
    }
  }

  import java.net.URL
  using(localPSQL) { connection =>
    val phrases = connection.make[Option[URL]]("select heading_id, business_id, url from listings") { row =>
      println("Heading=" + row("heading_id") + ", business=" + row.int("business_id"))
      try {
        Some(new URL(row.string(2)))
      } catch {
        case e => None
      }
    }
    println(phrases.filter(_.isDefined).map(_.get))
  }
}
