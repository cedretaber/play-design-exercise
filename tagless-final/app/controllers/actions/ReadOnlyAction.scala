package controllers.actions

import com.google.inject.ImplementedBy
import controllers.actions.impl.DefaultReadOnlyAction
import play.api.mvc._
import scalikejdbc.DBSession

import scala.concurrent.Future

@ImplementedBy(classOf[DefaultReadOnlyAction])
trait ReadOnlyAction[B] { self: ActionBuilderImpl[B] =>

  def asyncWithSession(f: (Request[AnyContent], DBSession) => Future[Result]): Action[AnyContent]

}