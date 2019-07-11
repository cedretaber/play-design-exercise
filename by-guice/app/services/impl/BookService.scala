package services.impl

import javax.inject.{Inject, Singleton}
import models.Book
import scalikejdbc.DBSession
import services.BookRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BookService @Inject()(bookDao: BookRepository[DBSession])
  extends services.BookService[(DBSession, ExecutionContext)] {

  override def finaAll()(implicit context: Context): Future[Seq[Book]] = {
    implicit val (dbs, ec) = context
    Future {
      bookDao.all()
    }
  }

  override def findById(id: Book.Id)(implicit context: Context): Future[Option[Book]] = {
    implicit val (dbs, ec) = context
    Future {
      bookDao.findById(id)
    }
  }
}
