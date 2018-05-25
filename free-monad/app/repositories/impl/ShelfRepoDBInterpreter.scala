package repositories.impl

import cats.data.State
import cats.~>
import models.Ids.ShelfId
import models.Shelf
import repositories.entities.Shelves
import repositories.{CustomParameterBinder, ShelfRepo}
import scalikejdbc._

object ShelfRepoDBInterpreter extends ShelfRepo.Interpreter[State[DBSession, ?]] with CustomParameterBinder {
  type DBState[X] = State[DBSession, X]

  override def get: ShelfRepo ~> DBState = new (ShelfRepo ~> DBState) {
    override def apply[A](fa: ShelfRepo[A]): DBState[A] =
      for {
        s <- State.get
      } yield {
        implicit val session = s
        import ShelfRepo.{All, FindById}
        fa match {
          case All => all()
          case FindById(id) => findById(id)
        }
      }
  }

  val s = Shelves.syntax("s")

  val toShelf = Shelves.apply(s.resultName) _

  def all()(implicit session: DBSession): Seq[Shelf] =
    withSQL {
      selectFrom(Shelves as s).orderBy(s.id.asc)
    }.map(toShelf.andThen(_.toModel)).list.apply

  def findById(id: ShelfId)(implicit session: DBSession): Option[Shelf] =
    withSQL {
      selectFrom(Shelves as s)
        .where.eq(s.id, id)
    }.map(toShelf.andThen(_.toModel)).single.apply
}
