package edu.uic.cs474.project.parsing

import org.eclipse.jdt.core.dom.CompilationUnit

import scala.collection.mutable.HashMap

/**
  * Class to generate a single sample in the dataset.
  * @param before The parsed code before it was changed.
  * @param beforeStart The starting char of the changed region in the original file.
  * @param beforeEnd The ending char of the changed region in the original file.
  * @param after The parsed code after it was changed.
  * @param afterStart The starting char of the changed region in the changed file.
  * @param afterEnd The ending char of the changed region in the changed file.
  */
class SampleGenerator(repoID:String,before:CompilationUnit,beforeStart:Int,beforeEnd:Int,after:CompilationUnit,afterStart:Int,afterEnd:Int) {

  /**
    * Generate the sample.
    * @return A string with the generated sample.
    */
  def generate() : String = {

    var sample = repoID + ","

    val beforeVisitor = new CodeVisitor(beforeStart,beforeEnd)
    before.accept(beforeVisitor)

    SampleGenerator.names.foreach(name => {

      sample += beforeVisitor.features.getOrElse(name,"0") + ","
    })

    val afterVisitor = new CodeVisitor(afterStart,afterEnd)
    after.accept(afterVisitor)

    SampleGenerator.names.foreach(name => {

      sample += afterVisitor.features.getOrElse(name,"0") + ","
    })

    sample.substring(0,sample.length-1)
  }

}

object SampleGenerator {


  //The feature names
  private val names = Array("CatchClause","ArrayAccess","ArrayCreation","Assignment","BooleanLiteral",
    "CastExpression","CharacterLiteral","ClassInstanceCreation","ConditionalExpression","CreationReference",
    "ExpressionMethodReference","FieldAccess","InstanceofExpression","LambdaExpression","MethodInvocation",
    "NullLiteral","NumberLiteral","ParenthesizedExpression","StringLiteral","SuperFieldAccess",
    "SuperMethodInvocation","SuperMethodReference","ThisExpression","TypeLiteral","TypeMethodReference",
    "VariableDeclarationExpression","Modifier","BreakStatement","ContinueStatement","DoStatement",
    "ForStatement","IfStatement","ElseStatement","ReturnStatement","SuperConstructorInvocation",
    "SwitchStatement","SwitchCase","SwitchDefault","EnhancedForStatement","SynchronizedStatement",
    "ThrowStatement","TryStatement","WhileStatement","PrimitiveType","ArrayType",
    "SimpleType","QualifiedType","WildcardType","ParameterizedType","TypeParameter",
    "*","/","%","+","-","<<",">>",">>>","<",">","<=",">=","==","!=","^","&","|","&&","||","++","--","!","~")

  //Feature renamings
  private val renaming = new HashMap[String,String]
  renaming("*") = "TIMES"
  renaming("/") = "DIVIDE"
  renaming("%") = "REMAINDER"
  renaming("+") = "PLUS"
  renaming("-") = "MINUS"
  renaming("<<") = "LEFT_SHIFT"
  renaming(">>") = "RIGHT_SHIFT_SIGNED"
  renaming(">>>") = "RIGHT_SHIFT_UNSIGNED"
  renaming("<") = "LESS"
  renaming(">") = "GREATER"
  renaming("<=") = "LESS_EQUALS"
  renaming(">=") = "GREATER_EQUALS"
  renaming("==") = "EQUALS"
  renaming("!=") = "NOT_EQUALS"
  renaming("^") = "XOR"
  renaming("&") = "AND"
  renaming("|") = "OR"
  renaming("&&") = "CONDITIONAL_AND"
  renaming("||") = "CONDITIONAL_OR"
  renaming("++") = "INCREMENT"
  renaming("--") = "DECREMENT"
  renaming("!") = "NOT"
  renaming("~") = "COMPLEMENT"

  //Generate the CSV file header with feature names
  def getHeader() : String = {

    var header = "RepositoryID,"

    SampleGenerator.names.foreach(name => {

      header += SampleGenerator.renaming.getOrElse(name,name) + "_B,"
    })

    SampleGenerator.names.foreach(name => {

      header += SampleGenerator.renaming.getOrElse(name,name) + "_A,"
    })

    header.substring(0,header.length-1)
  }
}