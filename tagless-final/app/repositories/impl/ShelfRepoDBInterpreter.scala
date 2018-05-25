package repositories.impl

import cats.data.Reader
import models.Ids.ShelfId
import models.Shelf
import repositories.entities.Shelves
import repositories.{CustomParameterBinder, ShelfRepoSym}
import scalikejdbc._

object ShelfRepoDBInterpreter extends CustomParameterBinder {
  type ShelfRepoDB[X] = Reader[DBSession, X]

  val s = Shelves.syntax("s")

  val toShelf = Shelves.apply(s.resultName) _

  implicit val sym: ShelfRepoSym[ShelfRepoDB] = new ShelfRepoSym[ShelfRepoDB] {
    override def all: ShelfRepoDB[Seq[Shelf]] =
      Reader { implicit session =>
        withSQL {
          selectFrom(Shelves as s).orderBy(s.id.asc)
        }.map(toShelf.andThen(_.toModel)).list.apply
      }

    override def findById(id: ShelfId): ShelfRepoDB[Option[Shelf]] =
      Reader { implicit session =>
        withSQL {
          selectFrom(Shelves as s)
            .where.eq(s.id, id)
        }.map(toShelf.andThen(_.toModel)).single.apply
      }
  }
}
