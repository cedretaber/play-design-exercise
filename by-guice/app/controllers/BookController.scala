package controllers

import controllers.actions.ReadOnlyAction
import entities._
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import scalikejdbc._
import services.BookService

import scala.concurrent.ExecutionContext

@Singleton
class BookController @Inject()(
  components: ControllerComponents,
  readOnlyAction: ReadOnlyAction,
  bookService: BookService[BookController.Context]
) extends AbstractController(components) {

  import BookController._

  implicit val ec = components.executionContext

  implicit def cont(implicit session: DBSession): Context = (session, ec)

  def index = readOnlyAction.asyncWithSession { _ => implicit session =>
    bookService.finaAll().map { books =>
      Ok(Json.toJson(books.map(Book.fromModel)))
    }
  }

  def show(id: Long) = readOnlyAction.asyncWithSession { _ => implicit session =>
    bookService.findById(models.Book.Id(id)).map {
      case Some(book) =>
        Ok(Json.toJson(Book.fromModel(book)))
      case None =>
        NotFound
    }
  }
}

object BookController {
  type Context = (DBSession, ExecutionContext)
}