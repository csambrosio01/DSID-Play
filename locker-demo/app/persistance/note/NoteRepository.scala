package persistance.note

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[SqlNoteRepository])
trait NoteRepository {
}
