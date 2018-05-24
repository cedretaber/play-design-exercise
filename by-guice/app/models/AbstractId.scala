package models

trait AbstractId[T] extends Any {
  def unId: T
}
