package edu.uic.cs474.project.downloading

import org.eclipse.jgit.diff.Edit

/**
  * Created by Alessandro on 04/12/16.
  */
class DiffData(_diffDataType: DiffDataType,
               _oldStartingLine: Int,
               _newStartingLine: Int,
               _oldEndingLine: Int,
               _newEndingLine: Int) {

  def diffDataType = _diffDataType
  def oldStartingLine = _oldStartingLine
  def newStartingLine = _newStartingLine
  def oldEndingLine = _oldEndingLine
  def newEndingLine = _newEndingLine
}

object DiffData {
  def matchDiffDataType(editType: Edit.Type): DiffDataType = editType match {
    case Edit.Type.DELETE => Delete
    case Edit.Type.INSERT => Insert
    case Edit.Type.REPLACE => Replace
    case _ => throw new IllegalArgumentException
  }
}