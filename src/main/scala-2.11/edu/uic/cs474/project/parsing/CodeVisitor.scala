package edu.uic.cs474.project.parsing

import org.eclipse.jdt.core.dom._

import scala.collection.mutable.HashMap

/**
  * Created by andrea on 02/12/16.
  */
class CodeVisitor(startChar:Int,endChar:Int) extends ASTVisitor {

  val features = new HashMap[String,Int]

  private def filter(name:String,start:Int,end:Int) : Boolean = {

    if(start >= startChar && start <= endChar || end <= endChar && end >= startChar) {
      features(name) = features.getOrElse(name, 0) + 1
    }

    true
  }

  //TODO add exceptions ??
  override def visit(node:CatchClause) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ArrayAccess) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ArrayCreation) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:Assignment) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:BooleanLiteral) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:CastExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:CharacterLiteral) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ClassInstanceCreation) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ConditionalExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:CreationReference) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ExpressionMethodReference) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:FieldAccess) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:InfixExpression) : Boolean = filter(node.getOperator.toString,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:InstanceofExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:LambdaExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:MethodInvocation) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:NullLiteral) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:NumberLiteral) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ParenthesizedExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:PostfixExpression) : Boolean = filter(node.getOperator.toString,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:PrefixExpression) : Boolean = filter(node.getOperator.toString,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:StringLiteral) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SuperFieldAccess) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SuperMethodInvocation) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SuperMethodReference) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ThisExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:TypeLiteral) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:TypeMethodReference) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:VariableDeclarationExpression) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:Modifier) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:BreakStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ContinueStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:DoStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ForStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:IfStatement) : Boolean = {
    filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)
    if(node.getElseStatement != null)
      filter("ElseStatement",node.getStartPosition,node.getStartPosition+node.getLength)
    true
  }

  override def visit(node:ReturnStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SuperConstructorInvocation) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SwitchStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SwitchCase) : Boolean = {
    if(node.isDefault)
      filter("SwitchDefault",node.getStartPosition,node.getStartPosition+node.getLength)
    else
      filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)
  }

  override def visit(node:EnhancedForStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SynchronizedStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ThrowStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:TryStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:WhileStatement) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:PrimitiveType) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ArrayType) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:SimpleType) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:QualifiedType) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:WildcardType) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:ParameterizedType) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

  override def visit(node:TypeParameter) : Boolean = filter(node.getClass.getSimpleName,node.getStartPosition,node.getStartPosition+node.getLength)

}
