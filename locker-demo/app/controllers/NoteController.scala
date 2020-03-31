package controllers

import javax.inject.Inject
import model.AddNote
import model.Note._
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.NoteService

import scala.concurrent.{ExecutionContext, Future}

class NoteController @Inject() (
                                 userAction: UserInfoAction,
                                 cc: ControllerComponents,
                                 noteService: NoteService
                               )
                               (
                                 implicit ec: ExecutionContext
                               )
  extends AbstractController(cc)
    with Logging {

  def createNote(): Action[AnyContent] = userAction.async { implicit request: UserRequest[AnyContent] =>
    request
      .userInfo
      .fold(
        Future.successful(
          Unauthorized("You must be logged in to create a note")
        )
      ) { user =>
        request
          .body
          .asJson
          .map(_.as[AddNote])
          .map { note =>
            noteService.addNoteToUser(note, user).map { addedNote =>
              Ok(Json.toJson(addedNote))
            }
          }
          .getOrElse(Future.successful(BadRequest("Bad json")))
    }
  }

}
