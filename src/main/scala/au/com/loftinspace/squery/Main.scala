package au.com.loftinspace.squery

import Connection._

object Main extends Application {
  val localPSQL = new Connection("jdbc:postgresql://localhost:5432/lab_development", "lab", "lab")

  using(localPSQL) { connection =>
    connection.use("select * from banned_phrases") { row =>
      println(row.toString)
    }
  }

  import java.net.URL
  using(localPSQL) { connection =>
    val phrases = connection.make[Option[URL]]("select url from listings") { row =>
      try {
        Some(new URL(row.string(0)))
      } catch {
        case e => None
      }
    }
    println(phrases.filter(_.isDefined))
  }
}
