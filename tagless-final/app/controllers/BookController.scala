package controllers

import models.Ids._
import play.api.mvc.{AbstractController, Action}
import services.BookServiceSym

trait BookController[A, R[_]] { self: AbstractController =>

  val sym: BookServiceSym[R]

  def runIndex(books: R[Seq[models.Book]]): Action[A]

  final def index: Action[A] = runIndex {
    sym.findAll
  }

  def runShow(book: R[Option[models.Book]]): Action[A]

  final def show(id: Long): Action[A] = runShow {
    sym.findById(id.asBookId)
  }
}
