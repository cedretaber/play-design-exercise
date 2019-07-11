package services

import com.google.inject.ImplementedBy
import models.Shelf

@ImplementedBy(classOf[daos.impl.daos.ShelfDao])
trait ShelfRepository[C] {
  type Context = C

  def all()(implicit context: Context): Seq[Shelf]

  def findById(id: Shelf.Id)(implicit context: Context): Option[Shelf]
}
