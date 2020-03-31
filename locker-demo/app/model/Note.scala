package model

import java.sql.Timestamp

import play.api.libs.json._

case class Note(
                 noteId: Option[Long],
                 userId: Long,
                 content: String,
                 description: String,
                 createdAt: Timestamp,
                 updatedAt: Timestamp
               )

case class AddNote(
                        userId: Option[Long],
                        content: String,
                        description: String
                      )

object Note {
  def timestampToLong(t: Timestamp): Long = t.getTime

  def longToTimestamp(dt: Long): Timestamp = new Timestamp(dt)

  implicit val timestampFormat: Format[Timestamp] = new Format[Timestamp] {
    def writes(t: Timestamp): JsValue = Json.toJson(timestampToLong(t))

    def reads(json: JsValue): JsResult[Timestamp] = Json.fromJson[Long](json).map(longToTimestamp)
  }

  implicit val noteFormat: OFormat[Note] = Json.format[Note]
  implicit val requestNoteRead: Reads[AddNote] = Json.reads[AddNote]
}
