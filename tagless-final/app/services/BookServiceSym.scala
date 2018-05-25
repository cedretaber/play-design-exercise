package services

import models.Book
import models.Ids.BookId

import scala.language.higherKinds

trait BookServiceSym[R[_]] {
  def findAll: R[Seq[Book]]
  def findById(id: BookId): R[Option[Book]]
}
