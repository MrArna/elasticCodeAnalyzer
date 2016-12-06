package edu.uic.cs474.project

import akka.actor.ActorSystem
import edu.uic.cs474.project.Master.Init
import edu.uic.cs474.project.parsing.{CodeParser, CodeVisitor}

/**
  * The entry point of our application.
  */
object Main extends App {

  print("Dataset creation started.....")

  val system = ActorSystem("GithubAnalyzer")
  val master =system.actorOf (Master.props(Config.numDownloaders,Config.numDiffers,Config.numParsers))

  master ! Init


}
