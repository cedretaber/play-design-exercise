package daos

import com.google.inject.ImplementedBy
import models.{Book, Shelf}

import scala.language.higherKinds

@ImplementedBy(classOf[impl.daos.BookDao])
trait BookDao[C] {
  type Context = C

  def all()(implicit context: Context): Seq[Book]

  def findById(id: Book.Id)(implicit context: Context): Option[Book]

  def create(name: String, shelfId: Shelf.Id)(implicit context: Context): Boolean

  def update(id: Book.Id, name: String, shelfId: Shelf.Id)(implicit context: Context): Boolean

  def delete(id: Book.Id)(implicit context: Context): Boolean
}
