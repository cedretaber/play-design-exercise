package models

case class Shelf(id: Shelf.Id, name: String, books: Seq[Book])

object Shelf {
  case class Id(unId: Long) extends AnyVal with AbstractId[Long]
}
