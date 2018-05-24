package controllers.actions

import javax.inject.Inject
import play.api.libs.typedmap.TypedKey
import play.api.mvc._
import scalikejdbc.{DB, DBSession}

import scala.concurrent.{ExecutionContext, Future}

class ReadOnlyAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {

  import ReadOnlyAction._

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    val session = DB.readOnlySession()
    block(request.addAttr[DBSession](dbSessionKey, session)).andThen { case _ => session.close() }
  }

  def asyncWithSession(f: Request[AnyContent] => DBSession => Future[Result]): Action[AnyContent] =
    async { req =>
      req.attrs.get(dbSessionKey)
        .fold[Future[Result]](Future.successful(Results.InternalServerError))(f(req))
    }
}

object ReadOnlyAction {
  val dbSessionKey: TypedKey[DBSession] = TypedKey("DBSession")
}