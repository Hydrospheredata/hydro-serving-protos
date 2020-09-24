import sbt.Keys._

organization := "io.hydrosphere"
name := "serving-grpc-scala"
version := IO.read(file("../version")).trim

scalaVersion := "2.12.8"
crossScalaVersions := Seq("2.12.8", "2.13.3")

publishMavenStyle := true

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.2",
  "org.scalatest" %% "scalatest" % "3.2.2" % "test",
  "io.spray" %% "spray-json" % "1.3.5" % "provided",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "com.google.api.grpc" % "googleapis-common-protos" % "0.0.3" % "protobuf"
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
