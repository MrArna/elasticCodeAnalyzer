package edu.uic.cs474.project

import edu.uic.cs474.project.downloading.DiffData

/**
  * Created by Alessandro on 02/12/16.
  */
//A trait representing messages
sealed trait Message

case class GetCommitsDiffDataMap(repoPath: String, newCommit: String) extends Message

case class SendCommitsDiffDataMap(diffDataMap: Map[(String, String), List[DiffData]]) extends Message