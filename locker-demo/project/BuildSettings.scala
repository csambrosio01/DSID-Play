import sbt.Keys._
import Dependencies._

object BuildSettings {

  lazy val settings = Seq (
    name := "locker-demo",
    organization := "com.usp",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= dependencies
  )
}
