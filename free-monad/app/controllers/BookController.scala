package controllers

import play.api.mvc.{AbstractController, Action}
import services.BookService

trait BookController[A] { self: AbstractController =>

  def runIndex(program: BookService.BookServiceF[Seq[models.Book]]): Action[A]

  final def index: Action[A] = runIndex {
    BookService.findAll()
  }

  def runShow(program: BookService.BookServiceF[Option[models.Book]]): Action[A]

  final def show(id: Long): Action[A] = runShow {
    BookService.findById(models.Book.Id(id))
  }
}
