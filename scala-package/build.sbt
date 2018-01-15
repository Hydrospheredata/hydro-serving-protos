import sbt.Keys._

lazy val appVersion = settingKey[String]("Version")


organization := "io.hydrosphere"
name := "serving-grpc-scala"
version := sys.props.getOrElse("appVersion", "dev")

scalaVersion := "2.12.4"

publishMavenStyle := true

libraryDependencies ++= Seq(
  "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf",
  "io.grpc" % "grpc-netty" % com.trueaccord.scalapb.compiler.Version.grpcJavaVersion,
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion
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
