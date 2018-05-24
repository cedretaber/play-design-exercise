package controllers

import controllers.entities._
import play.api.libs.json._
import play.api.mvc.{AbstractController, Result}
import services.BookService

trait BookController { self: AbstractController =>

  def indexProgram(f: BookService.BookServiceF[Seq[models.Book]] => Seq[models.Book]): Result =
    Ok(Json.toJson(f(BookService.findAll()).map(Book.fromModel)))

  def showProgram(id: Long)(f: BookService.BookServiceF[Option[models.Book]] => Option[models.Book]): Result =
    f(BookService.findById(models.Book.Id(id))) match {
      case Some(book) => Ok(Json.toJson(Book fromModel book))
      case None => NotFound
    }
}
