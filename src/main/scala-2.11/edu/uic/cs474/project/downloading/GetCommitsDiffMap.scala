package edu.uic.cs474.project.downloading

/**
  * Created by Alessandro on 02/12/16.
  */
//A trait representing messages
sealed trait Message

//Start the receiving actor
case class GetCommitsDiffMap(repoPath: String, oldCommit: String, newCommit: String) extends Message