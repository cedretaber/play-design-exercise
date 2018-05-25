package controllers.impl

import controllers.actions.ReadOnlyAction
import controllers.entities.Book
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import repositories.impl.BookRepoDBInterpreter
import services.BookService.BookServiceF
import services.impl.BookServiceRepoInterpreter

import scala.concurrent.Future

@Singleton
class BookController @Inject()(
  components: ControllerComponents,
  readOnlyAction: ReadOnlyAction[AnyContent]
) extends AbstractController(components) with controllers.BookController[AnyContent] {

  implicit val ec = components.executionContext

  override def runIndex(program: BookServiceF[Seq[models.Book]]): Action[AnyContent] =
    readOnlyAction.asyncWithSession { (_ , session) =>
      Future {
        val (_, books) =
          program
            .foldMap(BookServiceRepoInterpreter.get)
            .foldMap(BookRepoDBInterpreter.get)
            .run(session)
            .value
        Ok(Json.toJson(books.map(Book.fromModel)))
      }
    }

  override def runShow(program: BookServiceF[Option[models.Book]]): Action[AnyContent] =
    readOnlyAction.asyncWithSession { (_, session) =>
      Future {
        program
          .foldMap(BookServiceRepoInterpreter.get)
          .foldMap(BookRepoDBInterpreter.get)
          .run(session)
          .value match {
          case (_, Some(book)) =>
            Ok(Json.toJson(Book fromModel book))
          case (_, None) =>
            NotFound
        }
    }
  }
}