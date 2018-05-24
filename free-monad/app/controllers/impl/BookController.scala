package controllers.impl

import controllers.actions.ReadOnlyAction
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import repositories.interpreters.impl.BookRepoDBInterpreter
import services.interpreters.impl.BookServiceRepoInterpreter

import scala.concurrent.Future

@Singleton
class BookController @Inject()(
  components: ControllerComponents,
  readOnlyAction: ReadOnlyAction
) extends AbstractController(components) with controllers.BookController {

  implicit val ec = components.executionContext

  def index = readOnlyAction.asyncWithSession { _ => implicit session =>
    Future {
      indexProgram(
        _.foldMap(BookServiceRepoInterpreter.get)
          .foldMap(BookRepoDBInterpreter.get)
          .run(session)
          .value
          ._2
      )
    }
  }

  def show(id: Long) = readOnlyAction.asyncWithSession { _ => implicit session =>
    Future {
      showProgram(id)(
        _.foldMap(BookServiceRepoInterpreter.get)
          .foldMap(BookRepoDBInterpreter.get)
          .run(session)
          .value
          ._2
      )
    }
  }
}