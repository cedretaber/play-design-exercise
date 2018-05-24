package services

import cats.free.Free
import cats.free.Free.liftF
import models.Book

sealed trait BookService[A]

object BookService {

  private[services] case object FindAll extends BookService[Seq[Book]]
  private[services] case class FindById(id: Book.Id) extends BookService[Option[Book]]

  type BookServiceF[A] = Free[BookService, A]

  def findAll(): BookServiceF[Seq[Book]] = liftF(FindAll)
  def findById(id: Book.Id): BookServiceF[Option[Book]] = liftF(FindById(id))
}
