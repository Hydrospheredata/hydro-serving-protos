addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.2")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")
// addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")
// addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")
addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.7")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.3"
