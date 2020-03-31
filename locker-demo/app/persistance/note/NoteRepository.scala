package persistance.note

import com.google.inject.ImplementedBy
import model.{Note, AddNote}

import scala.concurrent.Future

@ImplementedBy(classOf[SqlNoteRepository])
trait NoteRepository {

  def create(note: AddNote): Future[Note]

  def findNotesByUserId(userId: Long): Future[Seq[Note]]

  def findNotesByTitle(title: String): Future[Seq[Note]]

  def findNoteById(noteId: Long): Future[Option[Note]]

  def update(noteId: Long, note: AddNote): Future[Note]
}
