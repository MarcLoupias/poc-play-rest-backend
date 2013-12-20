// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.1")

// for IntelliJ env
addSbtPlugin("com.github.mpeltonen" %% "sbt-idea" % "1.5.2")

// for build infos
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.2.5")
