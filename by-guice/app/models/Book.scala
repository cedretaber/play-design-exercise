package models

case class Book(id: Book.Id, name: String, shelfId: Shelf.Id)

object Book {
  case class Id(unId: Long) extends AnyVal with AbstractId[Long]
}