package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(userAction: UserInfoAction, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {}
