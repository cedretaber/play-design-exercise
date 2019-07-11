package daos.impl.daos

import daos.impl.entities.Books
import javax.inject.Singleton
import models.{Book, Shelf}
import scalikejdbc._
import services.BookRepository

@Singleton
class BookDao extends BookRepository[DBSession] with CustomParameterBinder {

  val b = Books.syntax("b")

  val toBook = Books(b.resultName) _

  val col = Books.column

  override def all()(implicit context: Context): Seq[models.Book] =
    withSQL {
      selectFrom(Books as b).orderBy(b.id.asc)
    }.map(toBook.andThen(_.toModel)).list.apply

  override def findById(id: Book.Id)(implicit context: Context): Option[Book] =
    withSQL {
      selectFrom(Books as b)
        .where.eq(b.id, id)
    }.map(toBook.andThen(_.toModel)).single.apply

  override def create(name: String, shelfId: Shelf.Id)(implicit context: Context): Boolean =
    withSQL {
      insertInto(Books).namedValues(
        col.name -> name,
        col.shelfId -> shelfId.unId
      )
    }.update.apply > 0

  override def update(id: Book.Id, name: String, shelfId: Shelf.Id)(implicit context: Context): Boolean =
    withSQL {
      QueryDSL.update(Books).set(
        col.name -> name,
        col.shelfId -> shelfId.unId
      )
        .where.eq(col.id, id)
    }.update.apply > 0

  override def delete(id: Book.Id)(implicit context: Context): Boolean =
    withSQL {
      deleteFrom(Books).where.eq(col.id, id)
    }.update.apply > 0
}
