package repositories.impl

import cats.data.Reader
import models.Ids.BookId
import models.{Ids, Shelf}
import repositories.{BookRepoSym, CustomParameterBinder}
import repositories.entities._
import scalikejdbc._

object BookRepoDBInterpreter extends CustomParameterBinder {
  type BookRepoDB[A] = Reader[DBSession, A]

  val b = Books.syntax("b")

  val toBook = Books(b.resultName) _

  val col = Books.column

  implicit val sym: BookRepoSym[BookRepoDB] = new BookRepoSym[BookRepoDB] {

    override def all: BookRepoDB[Seq[models.Book]] =
      Reader { implicit session =>
        withSQL {
          selectFrom(Books as b).orderBy(b.id.asc)
        }.map(toBook.andThen(_.toModel)).list.apply
      }

    override def findById(id: BookId): BookRepoDB[Option[models.Book]] =
      Reader { implicit session =>
        withSQL {
          selectFrom(Books as b)
            .where.eq(b.id, id)
        }.map(toBook.andThen(_.toModel)).single.apply
      }

    override def create(name: String, shelfId: Shelf.Id): BookRepoDB[Unit] =
      Reader { implicit session =>
        withSQL {
          insertInto(Books).namedValues(
            col.name -> name,
            col.shelfId -> shelfId.unId
          )
        }.update.apply
      }

    override def update(id: Ids.BookId, name: String, shelfId: Shelf.Id): BookRepoDB[Unit] =
      Reader { implicit session =>
        withSQL {
          QueryDSL.update(Books).set(
            col.name -> name,
            col.shelfId -> shelfId.unId
          )
            .where.eq(col.id, id)
        }.update.apply
      }

    override def delete(id: _root_.models.Book.Id): BookRepoDB[Unit] =
      Reader { implicit session =>
        withSQL {
          deleteFrom(Books).where.eq(col.id, id)
        }.update.apply
      }
  }
}
