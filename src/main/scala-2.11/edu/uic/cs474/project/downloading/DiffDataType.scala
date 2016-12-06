package edu.uic.cs474.project.downloading

/**
  * Created by Alessandro on 04/12/16.
  */
//A trait representing the type of modification made by a code block in a patch file
sealed trait DiffDataType

case object Insert extends DiffDataType
case object Replace extends DiffDataType
case object Delete extends DiffDataType


