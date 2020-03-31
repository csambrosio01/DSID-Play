package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class HomeController @Inject()(userAction: UserInfoAction, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {

  def consultSession: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val cookies = request.cookies.map( cookie => (cookie.name, cookie.value, cookie.path, cookie.domain))

    val json = Json.obj(
      "cookie" ->  Json.toJson(cookies),
      "session" -> request.session.get(SESSION_ID)
    )

    Ok(json)
  }
}
