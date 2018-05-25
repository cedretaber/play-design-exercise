package services

import cats.free.Free
import cats.free.Free.liftF
import cats.~>
import models.Book

import scala.language.higherKinds

sealed trait BookService[A]

object BookService {

  case object FindAll extends BookService[Seq[Book]]
  case class FindById(id: Book.Id) extends BookService[Option[Book]]

  type BookServiceF[A] = Free[BookService, A]

  def findAll(): BookServiceF[Seq[Book]] = liftF(FindAll)
  def findById(id: Book.Id): BookServiceF[Option[Book]] = liftF(FindById(id))

  trait Interpreter[M[_]] {
    def get: BookService ~> M
  }
}
