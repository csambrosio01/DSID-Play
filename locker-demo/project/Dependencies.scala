import sbt._
import play.sbt.PlayImport._

object Dependencies {

  private val playSlick = "com.typesafe.play" %% "play-slick" % "5.0.0"
  private val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"

  private val postgres = "org.postgresql" % "postgresql" % "42.2.11"

  val dependencies = Seq(
    playSlick, playSlickEvolutions, postgres, guice
  )
}
