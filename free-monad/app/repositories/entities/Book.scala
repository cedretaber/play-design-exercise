package repositories.entities

import scalikejdbc._

case class Book(id: models.Book.Id, name: String, shelfId: models.Shelf.Id) {

  lazy val toModel: models.Book =
    models.Book(id, name, shelfId)
}

object Books extends SQLSyntaxSupport[Book] {
  def apply(rn: ResultName[Book])(wr: WrappedResultSet): Book =
    Book(models.Book.Id(wr.long(rn.id)), wr.string(rn.name), models.Shelf.Id(wr.long(rn.shelfId)))
}