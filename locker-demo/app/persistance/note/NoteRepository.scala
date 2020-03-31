package persistance.note

import com.google.inject.ImplementedBy
import model.{Note, AddNote}

import scala.concurrent.Future

@ImplementedBy(classOf[SqlNoteRepository])
trait NoteRepository {

  def create(note: AddNote): Future[Note]
}
