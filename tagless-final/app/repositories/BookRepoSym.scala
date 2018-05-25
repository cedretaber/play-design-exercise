package repositories

import models.Ids.BookId
import models.{Book, Shelf}

import scala.language.higherKinds

trait BookRepoSym[R[_]] {
  def all: R[Seq[Book]]
  def findById(id: BookId): R[Option[Book]]
  def create(name: String, shelfId: Shelf.Id): R[Unit]
  def update(id: Book.Id, name: String, shelfId: Shelf.Id): R[Unit]
  def delete(id: Book.Id): R[Unit]
}