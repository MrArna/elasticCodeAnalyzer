package edu.uic.cs474.project.parsing

import java.io.{File, FileNotFoundException}
import java.util.Scanner

import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit

/**
  * Creates the AST of a given Java file.
  * @param path
  */
class CodeParser(path:String) {

  /**
    * Reads the file into a char array.
    *
    * @return The char array with the file content.
    * @throws FileNotFoundException In case the file is not found.
    */
  def readCode() : Array[Char] = {

    //Initialize the scanner
    val scanner = new Scanner(new File(path))
    var code = ""

    //Read file
    while (scanner.hasNextLine) {

      code = code + "\n" + scanner.nextLine()
    }

    scanner.close()
    //Return a Char array
    code.toCharArray()
  }

  /**
    * Parse the given file
    * @return The generated AST
    * @throws FileNotFoundException In case the file is not found.
    */
  def parse() : CompilationUnit = {

    //Read the code
    val source = readCode()

    //Create the parser
    val parser = ASTParser.newParser(AST.JLS8);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(source);

    //Create the AST
    parser.createAST(null).asInstanceOf[CompilationUnit];
  }

}
