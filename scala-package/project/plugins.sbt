addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.0-RC1")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.0")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.8"
