package model

import play.api.libs.json.{Json, OFormat}

case class User(userId: Option[Long],
                username: String,
                password: String,
                name: String,
                email: String,
                phoneNumber: Long)


object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
