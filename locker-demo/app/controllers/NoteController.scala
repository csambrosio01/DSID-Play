package controllers

import exception.{NotFoundException, UnauthorizedException}
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

  def getUserNotes(): Action[AnyContent] = userAction.async { implicit request: UserRequest[AnyContent] =>
    request
      .userInfo
      .fold(
        Future.successful(
          Unauthorized("You must be logged in to create a note")
        )
      ) { user =>
        val futureNotes = noteService.getUserNotes(user)

        futureNotes.map { notes =>
          if (notes.isEmpty) {
            logger.warn(s"There isn't a note related to user: ${user.userId.get}")
            NotFound("You don't have any note related to your user")
          } else {
            Ok(Json.toJson(notes))
          }
        }
      }
  }

  def getNotesByTitle(): Action[AnyContent] = userAction.async { implicit request: UserRequest[AnyContent] =>
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
          .map(_.as[String])
          .map { title =>
            val futureNotes = noteService.getNotesByTitle(title, user)

            futureNotes.map { notes =>
              if (notes.isEmpty) {
                logger.warn(s"There isn't a note with title $title related to user ${user.userId.get}")
                NotFound("You don't have any note related to your user")
              } else {
                Ok(Json.toJson(notes))
              }
            }
          }
          .getOrElse(Future.successful(BadRequest("Bad json")))
      }
  }

  def getNoteById(noteId: Long): Action[AnyContent] = userAction.async { implicit request: UserRequest[AnyContent] =>
    request
      .userInfo
      .fold(
        Future.successful(
          Unauthorized("You must be logged in to create a note")
        )
      ) { user =>
        val futureNote = noteService.getNoteById(noteId, user)

        futureNote.map { note =>
          if (note.isDefined) {
            Ok(Json.toJson(note))
          } else {
            logger.warn(s"There isn't a note with id $noteId related with user ${user.userId.get}")
            NotFound("You don't have a note with this id related to your user")
          }
        }
      }
  }

  def updateNote(noteId: Long): Action[AnyContent] = userAction.async { implicit request: UserRequest[AnyContent] =>
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
            val futureUpdatedNote = noteService.updateNote(note, noteId, user)

            futureUpdatedNote.map { updatedNote =>
              Ok(Json.toJson(updatedNote))
            }
              .recover {
                case e: NotFoundException =>
                  logger.warn(e.message)
                  NotFound("Could not found a note with given id")
                case e: UnauthorizedException =>
                  logger.warn(e.message)
                  Unauthorized("You can't update a note that isn't yours")
              }
          }
          .getOrElse(Future.successful(BadRequest("Bad json")))
      }
  }

}
