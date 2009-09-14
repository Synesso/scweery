package scweery

import java.util.Date

abstract class Field(val name: String) {
  type T
  def value: T
  override def toString = String valueOf value
  def s = value.asInstanceOf[String]
  def i = value.asInstanceOf[Int]
  def d = value.asInstanceOf[Double]
  def date = value.asInstanceOf[Date]
}

class StringField(name: String, f: String) extends Field(name) {
  type T = String
  override def value: T = f
}

class IntField(name: String, f: Int) extends Field(name) {
  type T = Int
  override def value: T = f
}

class DoubleField(name: String, f: Double) extends Field(name) {
  type T = Double
  override def value: T = f
}

class DateField(name: String, f: Date) extends Field(name) {
  type T = Date
  override def value: T = f
}

