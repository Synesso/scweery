package au.com.loftinspace.squery


import runtime.{RichDouble, RichInt, RichString}

abstract class Field {
  type T
  val value: T
  override def toString = String valueOf value
}

object StringField {
  implicit def unwrap(f: StringField) = f.value.asInstanceOf[String]
  implicit def unwrapRich(f: StringField) = new RichString(f.value.asInstanceOf[String])
}
class StringField(f: String) extends Field {
  type T = String
  val value: T = f
}

object IntField {
  implicit def unwrap(f: IntField) = f.value.asInstanceOf[Int]
  implicit def unwrapRich(f: IntField) = new RichInt(f.value.asInstanceOf[Int])
}

class IntField(f: Int) extends Field {
  type T = Int
  val value: T = f
}

object DoubleField {
  implicit def unwrap(f: DoubleField) = f.value.asInstanceOf[Double]
  implicit def unwrapRich(f: DoubleField) = new RichDouble(f.value.asInstanceOf[Double])
}

class DoubleField(f: Double) extends Field {
  type T = Double
  val value: T = f
}

