package models

object Ids {
  type BookId = Book.Id
  type ShelfId = Shelf.Id

  implicit class LongToId(val value: Long) extends AnyVal {
    def asBookId: BookId = Book.Id(value)
    def asShelfId: ShelfId = Shelf.Id(value)
  }
}
