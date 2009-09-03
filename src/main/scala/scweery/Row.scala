package scweery

class Row(val rownum: Int, val fields: List[Field]) {
  override def toString = "Row(" + rownum + ", " + map + ")"

  lazy val strings: List[String] = fields.map(_.toString)
  lazy val anys: List[Any] = fields.map(_.value)
  lazy val map: Map[String, Field] = fields.foldLeft(Map.empty[String, Field]) {(m, n) => m(n.name) = n}
  lazy val mapStrings: Map[String, String] = fields.foldLeft(Map.empty[String, String]) {(m, n) => m(n.name) = n.toString}

  def string(i: Int) = fields(i).s
  def string(name: String) = map(name).s

  def int(i: Int) = fields(i).i
  def int(name: String) = map(name).i

  def double(i: Int) = fields(i).d
  def double(name: String) = map(name).d

  def apply(i: Int) = anys(i)
  def apply(name: String) = map(name)
}