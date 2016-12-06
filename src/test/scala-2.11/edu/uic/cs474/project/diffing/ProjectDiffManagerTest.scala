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
    val path = "/Users/Alessandro/Dropbox/Universita/UIC/OOP/marco_arnaboldi_alessandro_pappalardo_andrea_tirinzoni_project/files/test"
    val oldCommit = ProjectDiffManager.getPreviousCommit(path, newCommit)
    assert(oldCommit == "2b802f6f403e66eeb69a2b1d68247ebc5e872a6a")
  }

  test("getDiffDataMap") {
    val newCommit = "8393be80226145448e1f55374f652fb2d3afb068"
    val path = "/Users/Alessandro/Dropbox/Universita/UIC/OOP/marco_arnaboldi_alessandro_pappalardo_andrea_tirinzoni_project/files/test"
    val dataDiffMap = ProjectDiffManager.getDiffDataMap(path, newCommit)
    println(dataDiffMap)
  }

}
