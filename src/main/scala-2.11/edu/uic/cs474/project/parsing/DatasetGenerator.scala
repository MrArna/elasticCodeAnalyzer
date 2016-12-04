package edu.uic.cs474.project.parsing

import java.io.{File, PrintWriter}

import scala.collection.mutable

/**
  * Created by andrea on 04/12/16.
  */
class DatasetGenerator {

  val samples = new mutable.HashSet[String]

  def addSample(beforePath:String,afterPath:String,beforeStartLine:Int,beforeEndLine:Int,afterStartLine:Int,afterEndLine:Int): Unit = {

    val beforeParser = new CodeParser(beforePath,beforeStartLine,beforeEndLine)
    val beforeTuple = beforeParser.parse()

    val afterParser = new CodeParser(afterPath,afterStartLine,afterEndLine)
    val afterTuple = afterParser.parse()

    val generator = new SampleGenerator(beforeTuple._1,beforeTuple._2,beforeTuple._3,afterTuple._1,afterTuple._2,afterTuple._3)
    samples += generator.generate()
  }

  def saveDataset(path:String): Unit = {

    val writer = new PrintWriter(new File(path))

    val header = SampleGenerator.getHeader()
    writer.write(header)

    samples.foreach(sample => {

      writer.write(sample)
    })

    writer.close()
  }
}
