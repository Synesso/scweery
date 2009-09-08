import scweery.Connection._
import scweery._
import java.util.Date
val db = new Connection("jdbc:postgresql://localhost:5432/scweery", "lab", "lab")

/*
val all = making[String](db) { c =>
  c.make[String]("select a_varchar, a_char, a_text from things") { r =>
    r.strings.mkString(" ")
  }.mkString("|")
}

using(db) { c =>
  val makeAndDiscard = c.make[String]("select a_varchar from things") { row =>
    row.string(0)
  }
  println(makeAndDiscard)
}
*/

val z = making[String](db) { c =>
  c.make[Date]("select a_datetime from things") { r =>
    r.date(0)
  }.map(_.toString).mkString(", ")
}
println(z)
