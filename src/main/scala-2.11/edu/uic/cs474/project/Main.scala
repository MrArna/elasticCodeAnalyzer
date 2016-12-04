package edu.uic.cs474.project

import edu.uic.cs474.project.parsing.{CodeParser, CodeVisitor}

/**
  * The entry point of our application.
  */
object Main extends App {

  println("Hello World")

  /*val cp = new CodeParser("/home/andrea/workspace/HW1/src/main/java/edu/uic/cs474/hw1/parsing/ProjectParser.java",1,10)
  val cu = cp.parse()
  val visitor = new CodeVisitor(0,1000000)
  cu.accept(visitor)

  visitor.features.keySet.foreach(k => println(k + "  ->  " + visitor.features.get(k).get))*/
}
