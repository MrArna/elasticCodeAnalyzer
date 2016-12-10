package edu.uic.cs474.project.downloading

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import edu.uic.cs474.project.downloading.ProjectDownloader.{GetIssue, Start}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.ExecutionContext.Implicits.global


class ProjectDownloaderTest extends TestKit(ActorSystem("DownloadTest",ConfigFactory.parseString(TestKitUsageSpec.config)))
  with DefaultTimeout
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
{

  "A Downloader" should {
    "clone the repo and return a parse message" in {
      import scala.concurrent.duration._
      within(20 seconds) {
        val actorRef = TestActorRef[ProjectDownloader]


        actorRef ? Start(1, "Java",0) onSuccess {
          case GetIssue(id, title, body, commit) =>
            println(id + " " + title + " " + body + " " + commit)
            expectMsgClass(classOf[GetIssue])
        }
      }
    }

  }

}

