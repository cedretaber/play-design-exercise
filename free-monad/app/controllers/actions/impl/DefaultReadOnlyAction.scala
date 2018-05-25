package controllers.actions.impl

import controllers.actions.ReadOnlyAction
import javax.inject.{Inject, Singleton}
import play.api.libs.typedmap.TypedKey
import play.api.mvc._
import scalikejdbc.{DB, DBSession}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DefaultReadOnlyAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser) with ReadOnlyAction[AnyContent] {

  val dbSessionKey: TypedKey[DBSession] = TypedKey("DBSession")

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    val session = DB.readOnlySession()
    block(request.addAttr[DBSession](dbSessionKey, session)).andThen { case _ => session.close() }
  }

  override def asyncWithSession(f: (Request[AnyContent], DBSession) => Future[Result]): Action[AnyContent] =
    async { req =>
      req.attrs.get(dbSessionKey)
        .fold[Future[Result]](Future.successful(Results.InternalServerError))(f(req, _))
    }
}
