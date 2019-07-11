package daos.impl.daos

import daos.impl.entities.Shelves
import javax.inject.Singleton
import models.Shelf
import scalikejdbc._
import services.ShelfRepository

@Singleton
class ShelfDao extends ShelfRepository[DBSession] with CustomParameterBinder {

  val s = Shelves.syntax("s")

  val toShelf = Shelves.apply(s.resultName) _

  override def all()(implicit context: Context): Seq[Shelf] =
    withSQL {
      selectFrom(Shelves as s).orderBy(s.id.asc)
    }.map(toShelf.andThen(_.toModel)).list.apply

  override def findById(id: Shelf.Id)(implicit context: Context): Option[Shelf] =
    withSQL {
      selectFrom(Shelves as s)
        .where.eq(s.id, id)
    }.map(toShelf.andThen(_.toModel)).single.apply

}
