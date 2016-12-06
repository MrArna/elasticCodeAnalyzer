package edu.uic.cs474.project

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinPool
import edu.uic.cs474.project.Master.Init
import edu.uic.cs474.project.diffing.ProjectDiffManager
import edu.uic.cs474.project.diffing.ProjectDiffManager.{GetCommitsDiffDataMap, SendCommitsDiffDataMap}
import edu.uic.cs474.project.downloading.ProjectDownloader
import edu.uic.cs474.project.downloading.ProjectDownloader.{GetIssue, Start}
import edu.uic.cs474.project.parsing.DiffManager

/**
  * Created by andrea on 02/12/16.
  */
class Master(downloaders: Int, differs: Int, parsers: Int) extends Actor {

  var downloadersRouter: ActorRef = null
  var parsersRouter: ActorRef = null
  var differssRouter: ActorRef = null


  def receive = {

    case Init => {
      downloadersRouter = context.actorOf(
        ProjectDownloader.props().withRouter(RoundRobinPool(downloaders)), name = "downloadersPool")

      differssRouter = context.actorOf(
        ProjectDiffManager.props().withRouter(RoundRobinPool(differs)), name = "differsPool")

      parsersRouter = context.actorOf(
        DiffManager.props().withRouter(RoundRobinPool(parsers)), name = "parsersPool")

      downloadersRouter ! Start(Config.numProject,Config.language)
    }

    case GetIssue(repoId,title,body,commitSHA) => {
      val currentDirectory = new java.io.File(".").getCanonicalPath
      differssRouter ! GetCommitsDiffDataMap(currentDirectory + Config.tempFolder + "/" + repoId,commitSHA)
    }

    case SendCommitsDiffDataMap(diffDataMap) => {
      parsersRouter ! SendCommitsDiffDataMap(diffDataMap)
    }
  }
}


object Master
{

  trait Receive
  case object Init extends Receive


  def props(downloaders: Int, differs: Int, parsers: Int): Props = Props(new Master(downloaders,differs,parsers))
}