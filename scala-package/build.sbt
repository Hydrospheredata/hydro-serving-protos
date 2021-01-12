import sbt.Keys._

organization := "io.hydrosphere"
name := "serving-grpc-scala"
version := IO.read(file("../version")).trim

scalaVersion := "2.13.2"
crossScalaVersions := Seq("2.13.2", "2.12.11")

publishMavenStyle := true

val circeVersion = "0.13.0"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "com.google.api.grpc" % "googleapis-common-protos" % "0.0.3" % "protobuf",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-optics" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

PB.protoSources in Compile := Seq(
  file("../src")
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

publishArtifact in Test := false
pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots/")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2/")
}
licenses := Seq(
  "Apache 2.0 License" -> url("https://github.com/Hydrospheredata/hydro-serving-protos/blob/master/LICENSE")
)

homepage := Some(url("https://github.com/Hydrospheredata/hydro-serving-protos"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/Hydrospheredata/hydro-serving-protos.git"),
    "https://github.com/Hydrospheredata/hydro-serving-protos.git"
  )
)

developers := List(
  Developer(
    id = "KineticCookie",
    name = "Bulat Lutfullin",
    url = url("https://github.com/KineticCookie"),
    email = "lb6557@gmail.com"
  )
)
