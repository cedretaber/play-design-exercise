package services

import com.google.inject.ImplementedBy
import models.Book
import models.Shelf

@ImplementedBy(classOf[daos.impl.daos.BookDao])
trait BookRepository[C] {
  type Context = C

  def all()(implicit context: Context): Seq[Book]

  def findById(id: Book.Id)(implicit context: Context): Option[Book]

  def create(name: String, shelfId: Shelf.Id)(implicit context: Context): Boolean

  def update(id: Book.Id, name: String, shelfId: Shelf.Id)(implicit context: Context): Boolean

  def delete(id: Book.Id)(implicit context: Context): Boolean
}
