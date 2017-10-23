name := "JenkinsStatusController"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scream3r" % "jssc" % "2.8.0",
  "io.circe" % "circe-parser_2.12" % "0.8.0",
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)