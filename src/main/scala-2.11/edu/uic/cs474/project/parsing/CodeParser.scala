package edu.uic.cs474.project.parsing

import java.io.{File, FileNotFoundException}
import java.util.Scanner

import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit

/**
  * Creates the AST of a given java file. Also, this class computes the starting and ending characters
  * based on the given starting and ending lines.
  * @param path The file path to be parsed.
  * @param startLine The line where the change begins.
  * @param endLine The line where the change ends.
  */
class CodeParser(path:String,startLine:Int,endLine:Int) {

  /**
    * Reads the file into a char array.
    *
    * @return The char array with the file content and the starting/ending characters.
    * @throws FileNotFoundException In case the file is not found.
    */
  def readCode() : (Array[Char],Int,Int) = {

    //Initialize the scanner
    val scanner = new Scanner(new File(path))
    var code = ""
    var lines = 0
    var startChar = 0
    var endChar = 0

    //Read file
    while (scanner.hasNextLine) {

      //Check whether we reached the start/end line
      if(lines == startLine)
        startChar = code.length
      if(lines == endLine)
        endChar = code.length

      code = code + "\n" + scanner.nextLine()
      lines += 1
    }

    scanner.close()
    //Return a Char array
    (code.toCharArray(),startChar,endChar)
  }

  /**
    * Parse the given file
    * @return The generated AST and the starting/ending characters
    * @throws FileNotFoundException In case the file is not found.
    */
  def parse() : (CompilationUnit,Int,Int) = {

    //Read the code
    val tuple = readCode()
    val source = tuple._1

    //Create the parser
    val parser = ASTParser.newParser(AST.JLS8)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setSource(source)

    //Create the AST
    (parser.createAST(null).asInstanceOf[CompilationUnit],tuple._2,tuple._3)
  }

}
