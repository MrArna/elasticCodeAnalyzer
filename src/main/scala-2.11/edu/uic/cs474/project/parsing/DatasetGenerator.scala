package edu.uic.cs474.project.parsing

import java.io.{File, FileOutputStream, PrintWriter}

import scala.collection.mutable

/**
  * Provides functions to generate a dataset.
  */
class DatasetGenerator {

  //The samples currently in the dataset
  private val samples = new mutable.ArrayBuffer[String]
  private var first = true

  /**
    * Add a new sample to the dataset.
    *
    * @param beforePath The path of the file before it was changed.
    * @param afterPath The path of the file after it is changed.
    * @param beforeStartLine The line where the modification starts in the original file.
    * @param beforeEndLine The line where the modification ends in the original file.
    * @param afterStartLine The line where the modification starts in the changed file.
    * @param afterEndLine The line where the modification ends in the changed file.
    */
  def addSample(beforePath:String,afterPath:String,beforeStartLine:Int,beforeEndLine:Int,afterStartLine:Int,afterEndLine:Int): Unit = {

    val beforeParser = new CodeParser(beforePath,beforeStartLine,beforeEndLine)
    val beforeTuple = beforeParser.parse()

    val afterParser = new CodeParser(afterPath,afterStartLine,afterEndLine)
    val afterTuple = afterParser.parse()

    val generator = new SampleGenerator(beforeTuple._1,beforeTuple._2,beforeTuple._3,afterTuple._1,afterTuple._2,afterTuple._3)
    samples += generator.generate()
  }

  /**
    * Save the current samples in CSV format on the specified file.
    *
    * @param path The path where to save the dataset.
    */
  def saveDataset(path:String): Unit = {

    val file = new File(path)
    val exist = file.exists()

    val writer = if(first && !exist) new PrintWriter(file)
    else new PrintWriter(new FileOutputStream(file,true))

    if(first && !exist) {
      val header = SampleGenerator.getHeader()
      writer.println(header)
    }

    first = false

    samples.foreach(sample => {

      writer.println(sample)
    })

    writer.close()

    println("Saved batch of " + samples.size + " samples")

    samples.clear()
  }
}
