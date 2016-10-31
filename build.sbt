name := """play-i18n-url"""

organization := "com.ejisan"

version := "1.0.0-SNAPSHOT"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-optimise", "-Xlint")

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.5.9" % Provided,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

publishTo := Some(Resolver.file("ejisan", file(Path.userHome.absolutePath+"/Development/repo.ejisan"))(Patterns(true, Resolver.mavenStyleBasePattern)))
