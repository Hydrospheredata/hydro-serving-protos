addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.2")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.7")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.4"
