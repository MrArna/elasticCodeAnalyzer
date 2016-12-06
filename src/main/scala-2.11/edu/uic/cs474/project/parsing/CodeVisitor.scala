package edu.uic.cs474.project.parsing

import org.eclipse.jdt.core.dom.{ASTVisitor, ArrayAccess, CatchClause, CompilationUnit}

import scala.collection.mutable.HashMap

/**
  * Created by andrea on 02/12/16.
  */
class CodeVisitor(startChar:Int,endChar:Int) extends ASTVisitor {

  val features = new HashMap[String,Int]

  private def filter(name:String,start:Int,end:Int) : Boolean = {

    if(start >= startChar && start <= endChar || end <= endChar && end >= startChar) {
      features(name) = features.getOrElse(name, 0) + 1
    }

    true
  }

  //TODO add exceptions ??
  override def visit(node:CatchClause) : Boolean = filter(node.getClass.getName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ArrayAccess) : Boolean = filter(node.getClass.getName,node.getStartPosition,node.getStartPosition+node.getLength)
}
