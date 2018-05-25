package repositories

import cats.free.Free
import cats.free.Free.liftF
import cats.~>
import models.{Book, Shelf}

import scala.language.higherKinds

sealed trait BookRepo[A]

object BookRepo {
  case object All extends BookRepo[Seq[Book]]
  case class FindById(id: Book.Id) extends BookRepo[Option[Book]]
  case class Create(name: String, shelfId: Shelf.Id) extends BookRepo[Unit]
  case class Update(id: Book.Id, name: String, shelfId: Shelf.Id) extends BookRepo[Unit]
  case class Delete(id: Book.Id) extends BookRepo[Unit]

  type BookRepoF[A] = Free[BookRepo, A]

  def all: BookRepoF[Seq[Book]] = liftF(All)
  def findById(id: Book.Id): BookRepoF[Option[Book]] = liftF(FindById(id))
  def create(name: String, shelfId: Shelf.Id): BookRepoF[Unit] = liftF(Create(name, shelfId))
  def update(id: Book.Id, name: String, shelfId: Shelf.Id): BookRepoF[Unit] = liftF(Update(id, name, shelfId))
  def delete(id: Book.Id): BookRepoF[Unit] = liftF(Delete(id))

  trait Interpreter[M[_]] {
    def get: BookRepo ~> M
  }
}