package daos

import com.google.inject.ImplementedBy
import models.Shelf

import scala.language.higherKinds

@ImplementedBy(classOf[impl.daos.ShelfDao])
trait ShelfDao[C] {
  type Context = C

  def all()(implicit context: Context): Seq[Shelf]

  def findById(id: Shelf.Id)(implicit context: Context): Option[Shelf]
}
