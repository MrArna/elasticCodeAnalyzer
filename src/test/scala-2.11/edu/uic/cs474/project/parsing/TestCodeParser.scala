package edu.uic.cs474.project.parsing

import org.scalatest.FunSuite

/**
  * Created by andrea on 04/12/16.
  */
class TestCodeParser extends FunSuite {

  test("Test readCode") {

    val cp = new CodeParser("files/test/testRead",2,5)
    val expected = "12345\nasdfghjkl\n67890\nqwertyuiop\nmnb\nvcx\nzxcvbnms\n".toCharArray
    val tuple = cp.readCode()

    assertResult(expected) {

      tuple._1
    }

    assertResult(6) {

      tuple._2
    }

    assertResult(37) {

      tuple._3
    }
  }

  test("Test parse") {

    val cp = new CodeParser("files/test/simple.java",3,4)
    val tuple = cp.readCode()
    println(tuple)
  }

}
