package controllers

import javax.inject.Inject
import model.User
import model.User._
import play.api.mvc._
import services.UserService
import services.session.SessionService

import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject() (
                                 userAction: UserInfoAction,
                                 sessionGenerator: SessionGenerator,
                                 cc: ControllerComponents,
                                 sessionService: SessionService,
                                 userService: UserService
                               )
                               (
                                 implicit ec: ExecutionContext
                               )
  extends AbstractController(cc) {

  def createUser(): Action[AnyContent] = Action.async { implicit request =>
    request
      .body
      .asJson
      .map(_.as[User])
      .map { user: User =>
        val validation = for {
          _ <- userService.createUser(user)
          (sessionId, encryptedCookie) <- sessionGenerator.createSession(user)
        } yield (sessionId, encryptedCookie)

        validation.map {
          case (sessionId, encryptedCookie) =>
            val session = request.session + (SESSION_ID -> sessionId)
            Ok("Logged in")
              .withSession(session)
              .withCookies(encryptedCookie)
        }
      }
      .getOrElse(Future.successful(BadRequest("Bad json")))
  }

  def logout: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    request.session.get(SESSION_ID).foreach { sessionId =>
      sessionService.delete(sessionId)
    }

    discardingSession {
      Ok("Logged out")
    }
  }
}
