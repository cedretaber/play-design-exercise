package controllers.impl

import cats.data.Reader
import controllers.actions.ReadOnlyAction
import controllers.entities.Book
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc._
import scalikejdbc.DBSession
import services.BookServiceSym
import services.impl.BookServiceRepoInterpreter

import scala.concurrent.Future

@Singleton
class BookController @Inject()(
  components: ControllerComponents,
  readOnlyAction: ReadOnlyAction[AnyContent]
) extends AbstractController(components)
  with controllers.BookController[AnyContent, Reader[DBSession, ?]] {

  type WithSession[A] = Reader[DBSession, A]

  override val sym: BookServiceSym[WithSession] =
    BookServiceRepoInterpreter[WithSession](repositories.impl.BookRepoDBInterpreter.sym).sym

  implicit val ec = components.executionContext

  override def runIndex(books: WithSession[Seq[models.Book]]): Action[AnyContent] =
    readOnlyAction.asyncWithSession { (_ , session) =>
      Future {
        Ok(Json.toJson(books(session).map(Book.fromModel)))
      }
    }

  override def runShow(maybeBook: WithSession[Option[models.Book]]): Action[AnyContent] =
    readOnlyAction.asyncWithSession { (_, session) =>
      Future {
        maybeBook(session) match {
          case Some(book) =>
            Ok(Json.toJson(Book fromModel book))
          case None =>
            NotFound
        }
    }
  }
}