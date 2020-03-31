package model

import play.api.libs.json.{Json, Reads}

case class Login(
                  username: String,
                  password: String
                )

object Login {
  val loginReads: Reads[Login] = Json.reads[Login]
}
