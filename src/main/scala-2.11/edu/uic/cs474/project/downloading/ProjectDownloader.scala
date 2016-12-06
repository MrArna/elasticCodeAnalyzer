package edu.uic.cs474.project.downloading

import java.io.File

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import edu.uic.cs474.project.downloading.ProjectDownloader.{GetIssue, IssueClosedWithoutCommit, Start}
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

    val auth = if(userUri.contains("?")) "&client_id=e2fecab74cfe360f9bff&client_secret=ad9a46c26f8ccf20e1e5e32b13e5a8c9a73eb2f1" else "?client_id=e2fecab74cfe360f9bff&client_secret=ad9a46c26f8ccf20e1e5e32b13e5a8c9a73eb2f1"

    val request:HttpRequest=
      HttpRequest(
        GET,
        uri = userUri + auth
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
    var privatRepo = false
    var repoId:BigInt = 0

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

            val repoJson = parseJson(request(oldRepo))

            if((repoJson \ "private").asInstanceOf[JBool].value)
            {
              privatRepo = true
            }
            else
            {
              privatRepo = false
              repoId = (repoJson \ "id").asInstanceOf[JInt].num
              "git clone " + (repoJson \ "clone_url").asInstanceOf[JString].s + " tmp/" + repoId !;
            }
          }
          if(!privatRepo)
          {
            val title = (item \ "title").asInstanceOf[JString].s
            val body = (item \ "body").asInstanceOf[JString].s
            val eventsJson = parseJson(request((item \ "events_url").asInstanceOf[JString].s))
            for(event <- eventsJson.asInstanceOf[JArray].arr)
            {
              if((event \ "event").asInstanceOf[JString].s.equals("closed"))
              {
                if((event \ "commit_id").isInstanceOf[JString])
                {
                  val commitSHA = (event \ "commit_id").asInstanceOf[JString].s
                  sender ! GetIssue(repoId,title,body,commitSHA)
                }
                else
                {
                  sender ! IssueClosedWithoutCommit(repoId,title,body)
                }
              }
            }
          }

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
  case class GetIssue(repoId:BigInt,issueTitle:String,issueBody:String,commitId:String) extends Send
  case class IssueClosedWithoutCommit(repoId:BigInt,issueTitle:String,issueBody:String) extends Send


  def props():Props = Props(new ProjectDownloader)
}


