package repositories.interpreters.impl

import cats.data.State
import cats.~>
import repositories.BookRepo
import repositories.entities._
import repositories.interpreters.{BookRepoInterpreter, CustomParameterBinder}
import scalikejdbc._

object BookRepoDBInterpreter extends BookRepoInterpreter[State[DBSession, ?]] with CustomParameterBinder {
  type DBState[X] = State[DBSession, X]

  def get: BookRepo ~> DBState = new (BookRepo ~> DBState) {
    override def apply[A](fa: BookRepo[A]): DBState[A] = {
      for {
        s <- State.get
      } yield {
        implicit val session: DBSession = s
        import BookRepo.{All, FindById, Create, Update, Delete}
        fa match {
          case All => all()
          case FindById(id) => findById(id)
          case Create(name, shelfId) => create(name, shelfId)
          case Update(id, name, shelfId) => update(id, name, shelfId)
          case Delete(id) => delete(id)
        }
      }
    }
  }

  val b = Books.syntax("b")

  val toBook = Books(b.resultName) _

  val col = Books.column

  private[this] def all()(implicit session: DBSession): Seq[models.Book] =
    withSQL {
      selectFrom(Books as b).orderBy(b.id.asc)
    }.map(toBook.andThen(_.toModel)).list.apply

  private[this] def findById(id: models.Book.Id)(implicit session: DBSession): Option[models.Book] =
    withSQL {
      selectFrom(Books as b)
        .where.eq(b.id, id)
    }.map(toBook.andThen(_.toModel)).single.apply

  private[this] def create(name: String, shelfId: models.Shelf.Id)(implicit session: DBSession): Unit =
    withSQL {
      insertInto(Books).namedValues(
        col.name -> name,
        col.shelfId -> shelfId.unId
      )
    }.update.apply

  private[this] def update(id: models.Book.Id, name: String, shelfId: models.Shelf.Id)(implicit session: DBSession): Unit =
    withSQL {
      QueryDSL.update(Books).set(
        col.name -> name,
        col.shelfId -> shelfId.unId
      )
        .where.eq(col.id, id)
    }.update.apply

  private[this] def delete(id: models.Book.Id)(implicit session: DBSession): Unit =
    withSQL {
      deleteFrom(Books).where.eq(col.id, id)
    }.update.apply
}
