package edu.uic.cs474.project.parsing

import org.scalatest.FunSuite

/**
  * Created by andrea on 04/12/16.
  */
class TestCodeVisitor extends FunSuite {

  test("Test Visitor 1") {

    val cp = new CodeParser("files/test/simple.java",1,6)
    val tuple = cp.parse()
    val visitor = new CodeVisitor(tuple._2,tuple._3)
    tuple._1.accept(visitor)

    assert(visitor.features.get("+").get==1)
    assert(visitor.features.get("*").get==1)
    assert(visitor.features.get("Assignment").get==2)
    assert(visitor.features.get("NumberLiteral").get==2)
    assert(visitor.features.get("Modifier").get==3)
  }

  test("Test Visitor 2") {

    val cp = new CodeParser("files/test/simple.java",3,4)
    val tuple = cp.parse()
    val visitor = new CodeVisitor(tuple._2,tuple._3)
    tuple._1.accept(visitor)

    assert(visitor.features.get("+").get==1)
    assert(visitor.features.get("*").get==1)
    assert(visitor.features.get("Assignment").get==2)
    assert(visitor.features.get("NumberLiteral").get==2)
    assert(visitor.features.get("Modifier").isEmpty)
  }
}
