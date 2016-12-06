package edu.uic.cs474.project

import akka.actor.ActorSystem
import edu.uic.cs474.project.Master.Init
import edu.uic.cs474.project.parsing.{CodeParser, CodeVisitor}

/**
  * The entry point of our application.
  */
object Main extends App {


  val system = ActorSystem("GithubAnalyzer")
  val master =system.actorOf (Master.props())

  master ! Init


  /*val cp = new CodeParser("/home/andrea/workspace/HW1/src/main/java/edu/uic/cs474/hw1/parsing/ProjectParser.java",1,10)
  val cu = cp.parse()
  val visitor = new CodeVisitor(0,1000000)
  cu.accept(visitor)

  visitor.features.keySet.foreach(k => println(k + "  ->  " + visitor.features.get(k).get))*/
}
