import sbt.Keys._

val circeVersion = "0.13.0"

val repoUser = sys.env.get("SONATYPE_USERNAME").getOrElse("")
val repoPass = sys.env.get("SONATYPE_PASSWORD").getOrElse("")
credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", repoUser, repoPass)

publishTo := sonatypePublishTo.value
organization := "io.hydrosphere"
name := "serving-grpc-scala"
version := sys.props.getOrElse("appVersion", IO.read(file("../version")).trim)
homepage := Some(url("https://github.com/Hydrospheredata/hydro-serving-protos"))
licenses := List("Apache-2.0" -> url("https://github.com/Hydrospheredata/hydro-serving-protos/blob/master/LICENSE"))
developers := List(
  Developer(
    "KineticCookie",
    "Bulat Lutfullin",
    "lb6557@gmail.com",
    url("https://github.com/KineticCookie")
  )
)
scalaVersion := "2.13.2"
crossScalaVersions := Seq("2.13.2", "2.12.11")
publishArtifact in Test := false
publishMavenStyle := true
pomIncludeRepository := { _ => false }
scmInfo := Some(
  ScmInfo(
    url("https://github.com/Hydrospheredata/hydro-serving-protos.git"),
    "https://github.com/Hydrospheredata/hydro-serving-protos.git"
  )
)
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
Global / pgpKeyRing := Some(file("~/secret/robot.gpg"))
