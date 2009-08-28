package au.com.loftinspace.squery


class Row(val rownum: Int, val fields: List[Field]) {
  override def toString = "Row(" + rownum + ", " + map + ")"

  def strings: List[String] = fields.map(_.toString)// todo - lazy load once only

  def anys: List[Any] = fields.map(_.value)// todo - lazy load once only

  def map: Map[String, Field] = fields.foldLeft(Map.empty[String, Field]) {(m, n) => m(n.name) = n} // todo - lazy load once only

  def mapStrings: Map[String, String] = fields.foldLeft(Map.empty[String, String]) {(m, n) => m(n.name) = n.toString}// todo - lazy load once only

  def string(i: Int) = fields(i).s
  def string(name: String) = map(name).s

  def int(i: Int) = fields(i).i
  def int(name: String) = map(name).i

  def double(i: Int) = fields(i).d
  def double(name: String) = map(name).d

  def apply(i: Int) = anys(i)
  def apply(name: String) = map(name)
}