package controllers

import javax.inject.Inject
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

  def login = userAction.async { implicit request: UserRequest[AnyContent] =>
    val successFunc = { userInfo: UserInfo =>
      sessionGenerator.createSession(userInfo).map {
        case (sessionId, encryptedCookie) =>
          val session = request.session + (SESSION_ID -> sessionId)
          Redirect(routes.HomeController.index())
            .withSession(session)
            .withCookies(encryptedCookie)
      }
    }

    val errorFunc = { badForm: Form[UserInfo] =>
      Future.successful {
        BadRequest(views.html.index(badForm)).flashing(FLASH_ERROR -> "Could not login!")
      }
    }

    form.bindFromRequest().fold(errorFunc, successFunc)
  }


  def logout = Action { implicit request: Request[AnyContent] =>
    // When we delete the session id, removing the session id is enough to render the
    // user info cookie unusable.
    request.session.get(SESSION_ID).foreach { sessionId =>
      sessionService.delete(sessionId)
    }

    discardingSession {
      Redirect(routes.HomeController.index())
    }
  }

}
