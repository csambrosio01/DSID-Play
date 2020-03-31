package persistance.note

import java.sql.Timestamp

import javax.inject.Inject
import model.Note
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

private class NoteTable(tag: Tag) extends Table[Note](tag, "notes") {

  def noteId = column[Option[Long]]("note_id", O.PrimaryKey, O.AutoInc)

  def userId = column[Long]("user_id")

  def content = column[String]("content")

  def description = column[String]("description")

  def createdAt = column[Timestamp]("created_at")

  def updatedAt = column[Timestamp]("updated_at")

  def * = (noteId, userId, content, description, createdAt, updatedAt) <> ((Note.apply _).tupled, Note.unapply)
}

class SqlNoteRepository @Inject()(
                                   protected val dbConfigProvider: DatabaseConfigProvider
                                 )
                                 (
                                   implicit ec: ExecutionContext
                                 )
  extends HasDatabaseConfigProvider[JdbcProfile]
    with NoteRepository {

  import profile.api._

  private val notes = TableQuery[NoteTable]
}
