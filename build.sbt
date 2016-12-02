name := "Project"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.11"

libraryDependencies += "com.typesafe.akka" % "akka-http-core_2.11" % "3.0.0-RC1"

libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.10"

parallelExecution in Test := false