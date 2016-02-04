name := """Ticket-Management-System"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "org.webjars" % "jquery" % "2.0.0"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "2.13.0"
// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
herokuAppName in Compile := "ticket-management-system"
