import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    name := "ReminderMail",
    version := "0.2",
    versionCode := 2,
    scalaVersion := "2.9.0-1",
    platformName in Android := "android-7"
  )

  lazy val fullAndroidSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "jqno",
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    "ReminderMail",
    file("."),
    settings = General.fullAndroidSettings ++ AndroidManifestGenerator.settings
  )

  lazy val tests = Project (
    "tests",
    file("tests"),
    settings = General.settings ++ AndroidTest.androidSettings ++ Seq (
      name := "ReminderMailTests"
    )
  ) dependsOn main
}
