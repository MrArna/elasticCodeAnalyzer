package edu.uic.cs474.project

/**
  * The global configuration.
  */
object Config {

  //The language that projects to be downloaded should be written in
  var language:String = "Java"

  //The folder where downloaded and temporary files should be created
  val tempFolder:String = "tmp"
  //The path of the dataset to be generated
  var datasetPath:String = "files/dataset.csv"

  val numProject:Int = 1000

  val shift = 10

  val numDownloaders = 1
  val numParsers = 1
  val numDiffers = 1


}
