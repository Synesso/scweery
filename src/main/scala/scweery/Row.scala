package scweery

/** Represents a single row returned from a SQL query */
class Row(val rownum: Int, val fields: List[Field]) {
  override def toString = "Row(" + rownum + ", " + map + ")"

  /** All columns in this row returned in order, as Strings */
  lazy val strings: List[String] = fields.map(_.toString)
  /** All columns in this row returned in order, as instances of Any */
  lazy val anys: List[Any] = fields.map(_.value)
  /** All columns in this row returned as a map from column name -> Any */
  lazy val map: Map[String, Field] = fields.foldLeft(Map.empty[String, Field]) {(m, n) => m + (n.name -> n)}
  /** All columns in this row returned as a map from column name -> String */
  lazy val mapStrings: Map[String, String] = fields.foldLeft(Map.empty[String, String]) {(m, n) => m + (n.name -> n.toString )}

  /** The value of column with index i, cast to a String */
  def string(i: Int) = fields(i).s
  /** The value of column with the given name, cast to a String */
  def string(name: String) = map(name).s

  /** The value of column with index i, cast to an Int */
  def int(i: Int) = fields(i).i
  /** The value of column with the given name, cast to a Int */
  def int(name: String) = map(name).i

  /** The value of column with index i, cast to a Double */
  def double(i: Int) = fields(i).d
  /** The value of column with the given name, cast to a Double */
  def double(name: String) = map(name).d

  /** The value of column with index i, cast to a Date */
  def date(i: Int) = fields(i).date
  /** The value of column with the given name, cast to a Date */
  def date(name: String) = map(name).date

  /** The value of column with index i */
  def apply(i: Int) = anys(i)
  /** The value of column with the given name */
  def apply(name: String) = map(name)
}
