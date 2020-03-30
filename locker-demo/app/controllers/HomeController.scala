package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(userAction: UserInfoAction, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = userAction { implicit request: UserRequest[_] =>
    Ok(views.html.index(form))
  }
}
