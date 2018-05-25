package services.impl

import models.Book
import models.Ids.BookId
import repositories.BookRepoSym
import services.BookServiceSym

import scala.language.implicitConversions

case class BookServiceRepoInterpreter[R[_]](repoSym: BookRepoSym[R]) {
  type BookServiceRepo[A] = R[A]

  implicit val sym: BookServiceSym[R] = new BookServiceSym[R] {

    override def findAll: R[Seq[Book]] = repoSym.all

    override def findById(id: BookId): R[Option[Book]] = repoSym.findById(id)
  }
}