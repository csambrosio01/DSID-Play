package services

import exception.{NotFoundException, UnauthorizedException}
import javax.inject.Inject
import model.{AddNote, Note, User}
import persistance.note.NoteRepository

import scala.concurrent.{ExecutionContext, Future}

class NoteService @Inject()(
                             noteRepository: NoteRepository
                           )
                           (
                             implicit ec: ExecutionContext
                           ) {

  def addNoteToUser(note: AddNote, user: User): Future[Note] = {
    noteRepository.create(note.copy(userId = user.userId))
  }

  def getUserNotes(user: User): Future[Seq[Note]] = {
    noteRepository.findNotesByUserId(user.userId.get)
  }

  def getNotesByTitle(title: String, user: User): Future[Seq[Note]] = {
    noteRepository.findNotesByTitle(title)
      .map { notes =>
        if (notes.nonEmpty) {
          notes.filter(_.userId == user.userId.get)
        } else {
          notes
        }
      }
  }

  def getNoteById(noteId: Long, user: User): Future[Option[Note]] = {
    noteRepository.findNoteById(noteId)
      .map { note =>
        note.fold(note) { n => if (n.userId == user.userId.get) note else None }
      }
  }

  def updateNote(addNote: AddNote, noteId: Long, user: User): Future[Note] = {
    noteRepository.findNoteById(noteId)
      .map(_.getOrElse(throw NotFoundException(s"Could not found a note with id $noteId")))
      .flatMap { note =>
        if (note.userId == user.userId.get) {
          noteRepository.update(noteId, addNote)
        } else {
          throw UnauthorizedException(s"Note with is $noteId does not belongs to user ${user.userId.get}")
        }
      }
  }
}
