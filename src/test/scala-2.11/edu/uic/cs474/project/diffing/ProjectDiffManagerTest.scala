package edu.uic.cs474.project.diffing

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.FunSuite

/**
  * Created by Alessandro on 05/12/16.
  */
class ProjectDiffManagerTest extends FunSuite {

  test("testGetPreviousCommit") {
    val newCommit = "8393be80226145448e1f55374f652fb2d3afb068"
    val path = "."
    val oldCommit = ProjectDiffManager.getPreviousCommit(path, newCommit)
    assert(oldCommit == "2b802f6f403e66eeb69a2b1d68247ebc5e872a6a")
  }

  test("getDiffDataMap") {
    val newCommit = "8393be80226145448e1f55374f652fb2d3afb068"
    val path = "."
    val dataDiffMap = ProjectDiffManager.getDiffDataMap(path, newCommit)
    val firstDiff = new DiffData(Delete, 3, 3, 4, 3)
    val secondDiff = new DiffData(Delete, 5, 4, 6, 4)
    val thirdDiff = new DiffData(Replace, 7, 5, 8, 7)
    val diffList: List[DiffData] = List(firstDiff, secondDiff, thirdDiff)
    val computedDiffList: List[DiffData] = dataDiffMap.get("./simple2.java", "./simple2.java").get
    assert(computedDiffList == diffList)
  }
}
