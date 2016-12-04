package edu.uic.cs474.project.downloading

import java.io.File

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import edu.uic.cs474.project.downloading.ProjectDownloader.{PrivateRepoFound, Start}
import org.json4s.JsonAST.{JArray, JBool, JInt}
import org.json4s.{DefaultFormats, JString, JValue}
import org.json4s.jackson._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.sys.process._
import util.control.Breaks._

/**
  * Created by andrea on 23/10/16.
  */


class ProjectDownloader extends Actor with ActorLogging {

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))
  implicit val formats = DefaultFormats


  val http = Http(context.system)


  private def request(userUri: String): String = {
    val http = Http(context.system)
    import HttpMethods._
    //initialize the HTTP request
    val userData = ByteString("abc")

    val request:HttpRequest=
      HttpRequest(
        GET,
        uri = userUri
      )

    //make the request
    val fut : Future[HttpResponse] = http.singleRequest(request)

    //wait for response
    val response = Await.result(fut,Duration.Inf)

    //move response data in a flow in order to retrieve a string
    val src : Source[ByteString,Any] = response.entity.dataBytes
    val stringFlow : Flow[ByteString,String, NotUsed] = Flow[ByteString].map(chunk => chunk.utf8String)
    val sink : Sink[String,Future[String]] = Sink.fold("")(_ + _)

    //run the flow
    val content : RunnableGraph[Future[String]] = (src via stringFlow toMat sink) (Keep.right)

    val aggregation : Future[String] = content.run()

    //wait for the completion of the aggregation flow
    Await.result(aggregation,Duration.Inf)
    aggregation.value.get.get

  }



  def download(numOfProjects: Int, lang: String) = {


    var counter = 0
    var oldRepo = ""
    var newRepo = ""

    //create tmp directory if not exists
    val currentDirectory = new java.io.File(".").getCanonicalPath
    val tmpDir = new File(currentDirectory + "/tmp")

    if(!tmpDir.exists())
    {
      tmpDir.mkdir()
    }

    for(i <- 1 to (numOfProjects/101)+1)
    {
      //response into  json
      val json = parseJson(request("https://api.github.com/search/issues?q=label:bug+language:"+ lang +"+state:closed&sort=created&order=desc&page="+ i +"&per_page=100"))

      breakable
      {
        for(item <- (json \ "items").asInstanceOf[JArray].arr)
        {
          newRepo = (item \ "repository_url").asInstanceOf[JString].s
          if(!newRepo.equals(oldRepo))
          {
            oldRepo = newRepo
            counter += 1
            if(counter > numOfProjects) break;
          }
          val title = (item \ "title").asInstanceOf[JString].s
          val body = (item \ "body").asInstanceOf[JString].s
          println(prettyJson(item))


          val repoJson = parseJson(request(oldRepo))

          if((repoJson \ "private").asInstanceOf[JBool].value)
          {
            sender ! PrivateRepoFound
          }
          else
          {
            val repoId = (repoJson \ "id").asInstanceOf[JInt].num
            "git clone " + (item \ "clone_url").asInstanceOf[JString].s + " tmp/" + repoId !;
          }



          /*if((item \ "private").asInstanceOf[JBool].value)
          {
            sender ! PrivateRepoFound
          }
          else
          {
            val repoId = (item \ "id").asInstanceOf[JInt].num
            val owner = (item \ "owner" \ "id").asInstanceOf[JInt].num
            val issueListJson = parseJson(request("https://api.github.com/repos/"+ owner + "/" + repoId + "/issues"))
            println(issueListJson)

            //"git clone " + (item \ "clone_url").asInstanceOf[JString].s + " tmp/" + repoId !;
          }*/
        }
      }
    }
  }

  def receive = {
    case Start(numOfProject,lang) =>
      download(numOfProject,lang)
  }

}


object ProjectDownloader
{

  trait Receive
  case class Start(numOfProject: Int,lang:String) extends Receive

  trait Send
  case object PrivateRepoFound extends Send
  case class DownloadedRepo(id: Int,path:String,issues: List[(String,String)]) extends Send


  def props():Props = Props(new ProjectDownloader)
}


