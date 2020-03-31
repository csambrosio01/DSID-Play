package services

import javax.inject.Inject
import model.{Note, AddNote, User}
import persistance.note.NoteRepository

import scala.concurrent.Future

class NoteService @Inject()(
                             noteRepository: NoteRepository
                           ) {

  def addNoteToUser(note: AddNote, user: User): Future[Note] = {
    noteRepository.create(note.copy(userId = user.userId))
  }
}
