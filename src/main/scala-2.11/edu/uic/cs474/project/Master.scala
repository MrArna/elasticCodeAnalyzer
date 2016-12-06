package edu.uic.cs474.project

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinPool
import edu.uic.cs474.project.Master.Init
import edu.uic.cs474.project.downloading.ProjectDownloader

/**
  * Created by andrea on 02/12/16.
  */
class Master(downloaders: Int, versioners: Int, parsers: Int) extends Actor {

  var downloadersRouter: ActorRef = null
  var parsersRouter: ActorRef = null
  var versionersRouter: ActorRef = null


  def receive = {

    case Init => {
      downloadersRouter = context.actorOf(
        ProjectDownloader.props().withRouter(RoundRobinPool(downloaders)), name = "downloadersPool")

      //TODO fix props
      versionersRouter = context.actorOf(
        ProjectDownloader.props().withRouter(RoundRobinPool(downloaders)), name = "downloadersPool")

      //TODO fix props
      parsersRouter = context.actorOf(
        ProjectDownloader.props().withRouter(RoundRobinPool(downloaders)), name = "downloadersPool")
    }

  }
}


object Master
{

  trait Receive
  case object Init extends Receive


  def props(downloaders: Int, versioners: Int, parsers: Int): Props = Props(new Master(downloaders,versioners,parsers))
}