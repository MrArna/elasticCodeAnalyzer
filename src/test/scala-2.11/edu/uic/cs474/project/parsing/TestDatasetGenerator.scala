package edu.uic.cs474.project.parsing

import java.io.File

import org.scalatest.FunSuite

import scala.io.Source

/**
  * Created by andrea on 04/12/16.
  */
class TestDatasetGenerator extends FunSuite {

  test("Test DatasetGenerator 1") {

    val f = new File("files/test/dataset")
    if(f.exists()) f.delete()

    val dg = new DatasetGenerator

    dg.addSample("repo0","files/test/simple.java","files/test/simple.java",3,4,3,4)
    dg.addSample("repo0","files/test/simple.java","files/test/simple.java",3,4,3,4)
    dg.saveDataset("files/test/dataset")
    dg.addSample("repo0","files/test/simple.java","files/test/simple.java",3,4,3,4)
    dg.saveDataset("files/test/dataset")

    val lines = Source.fromFile("files/test/dataset").getLines()
    assert(Source.fromFile("files/test/dataset").getLines().size==4)
    val l1 = lines.next
    val l2 = lines.next
    val l3 = lines.next
    val l4 = lines.next

    assert(l2==l3)
    assert(l3==l4)
  }
}
