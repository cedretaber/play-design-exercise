package services.impl

import cats.~>
import models.Book
import repositories.BookRepo
import services.BookService

object BookServiceRepoInterpreter extends BookService.Interpreter[BookRepo.BookRepoF] {
  def get: (BookService ~> BookRepo.BookRepoF) = new (BookService ~> BookRepo.BookRepoF) {
    override def apply[A](fa: BookService[A]): BookRepo.BookRepoF[A] = {
      import BookService.{FindAll, FindById}
      fa match {
        case FindAll => findAll()
        case FindById(id) => findById(id)
      }
    }
  }

  private[this] def findAll(): BookRepo.BookRepoF[Seq[Book]] = BookRepo.all
  private[this] def findById(id: Book.Id): BookRepo.BookRepoF[Option[Book]] = BookRepo.findById(id)
}
