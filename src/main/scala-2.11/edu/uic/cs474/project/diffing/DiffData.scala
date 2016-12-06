package edu.uic.cs474.project.diffing

import org.eclipse.jgit.diff.Edit

/**
  * Created by Alessandro on 04/12/16.
  */
class DiffData(diffDataType: DiffDataType,
               oldStartingLine: Int,
               newStartingLine: Int,
               oldEndingLine: Int,
               newEndingLine: Int)

object DiffData {
  def matchDiffDataType(editType: Edit.Type): DiffDataType = editType match {
    case Edit.Type.DELETE => Delete
    case Edit.Type.INSERT => Insert
    case Edit.Type.REPLACE => Replace
    case _ => throw new IllegalArgumentException
  }
}