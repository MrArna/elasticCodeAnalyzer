name := "Project"

version := "1.0"

scalaVersion := "2.11.8"

scalaVersion := "2.11.8"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.11"

libraryDependencies += "com.typesafe.akka" % "akka-http-core_2.11" % "3.0.0-RC1"

libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.10"

libraryDependencies += "org.jgrapht" % "jgrapht-core" % "1.0.0"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5"

libraryDependencies += "com.typesafe.akka" % "akka-http-xml-experimental_2.11" % "2.4.11"

libraryDependencies += "org.json4s" % "json4s-jackson_2.11" % "3.4.2"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.8.5"

// https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit
libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.5.0.201609210915-r"

// https://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "commons-io" % "commons-io" % "2.5"

parallelExecution in Test := false

//mainClass in assembly := Some("edu.uic.cs474.hw3.Main")
