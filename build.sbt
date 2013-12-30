name := "poc-play-rest-backend"

version := "1.1-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.25",
  "org.hibernate" % "hibernate-entitymanager" % "4.2.1.Final",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "com.jayway.restassured" % "rest-assured" % "1.8.1" % "test" withSources(),
  "com.intellij" % "annotations" % "5.1"
  )

play.Project.playJavaSettings

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "appInfos"
