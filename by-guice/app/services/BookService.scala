package services

import com.google.inject.ImplementedBy
import models.Book

import scala.concurrent.Future
import scala.language.higherKinds

@ImplementedBy(classOf[impl.BookService])
trait BookService[C] {
  type Context = C

  def finaAll()(implicit context: Context): Future[Seq[Book]]

  def findById(id: Book.Id)(implicit context: Context): Future[Option[Book]]
}
