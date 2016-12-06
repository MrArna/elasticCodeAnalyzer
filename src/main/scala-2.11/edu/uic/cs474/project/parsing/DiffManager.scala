package edu.uic.cs474.project.parsing

import akka.actor.Actor
import edu.uic.cs474.project.{Config, SendCommitsDiffDataMap}

/**
  * Actor that receives parsed diffs and generates samples.
  */
class DiffManager extends Actor {

  //The dataset generator
  val generator = new DatasetGenerator

  def receive = {

    //Receive a new list of diffs
    case SendCommitsDiffDataMap(map) =>

      //Add each diff as a new sample
      map.foreach( x => {

        x._2.foreach(y => {

          generator.addSample(x._1._1,x._1._2,y.oldStartingLine,y.oldEndingLine,y.newStartingLine,y.newEndingLine)
        })
      })

    //Stop the actor and save the dataset
    case DiffManager.Stop =>

      generator.saveDataset(Config.datasetPath)
  }
}

object DiffManager {

  //Receive messages
  trait Receive
  case object Stop extends Receive

  //Send messages
  trait Send
}
