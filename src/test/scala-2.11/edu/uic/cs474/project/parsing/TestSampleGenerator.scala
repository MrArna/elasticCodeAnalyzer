package edu.uic.cs474.project.parsing

import org.scalatest.FunSuite

/**
  * Created by andrea on 04/12/16.
  */
class TestSampleGenerator extends FunSuite {

  test("Test SampleGenerator 1") {

    val cp = new CodeParser("files/test/simple.java",3,4)
    val tuple = cp.parse()
    val sg = new SampleGenerator("0",tuple._1,tuple._2,tuple._3,tuple._1,tuple._2,tuple._3)

    assertResult("RepositoryID,CatchClause_B,ArrayAccess_B,ArrayCreation_B,Assignment_B,BooleanLiteral_B,CastExpression_B,CharacterLiteral_B,ClassInstanceCreation_B,ConditionalExpression_B,CreationReference_B,ExpressionMethodReference_B,FieldAccess_B,InstanceofExpression_B,LambdaExpression_B,MethodInvocation_B,NullLiteral_B,NumberLiteral_B,ParenthesizedExpression_B,StringLiteral_B,SuperFieldAccess_B,SuperMethodInvocation_B,SuperMethodReference_B,ThisExpression_B,TypeLiteral_B,TypeMethodReference_B,VariableDeclarationExpression_B,Modifier_B,BreakStatement_B,ContinueStatement_B,DoStatement_B,ForStatement_B,IfStatement_B,ElseStatement_B,ReturnStatement_B,SuperConstructorInvocation_B,SwitchStatement_B,SwitchCase_B,SwitchDefault_B,EnhancedForStatement_B,SynchronizedStatement_B,ThrowStatement_B,TryStatement_B,WhileStatement_B,PrimitiveType_B,ArrayType_B,SimpleType_B,QualifiedType_B,WildcardType_B,ParameterizedType_B,TypeParameter_B,TIMES_B,DIVIDE_B,REMAINDER_B,PLUS_B,MINUS_B,LEFT_SHIFT_B,RIGHT_SHIFT_SIGNED_B,RIGHT_SHIFT_UNSIGNED_B,LESS_B,GREATER_B,LESS_EQUALS_B,GREATER_EQUALS_B,EQUALS_B,NOT_EQUALS_B,XOR_B,AND_B,OR_B,CONDITIONAL_AND_B,CONDITIONAL_OR_B,INCREMENT_B,DECREMENT_B,NOT_B,COMPLEMENT_B,CatchClause_A,ArrayAccess_A,ArrayCreation_A,Assignment_A,BooleanLiteral_A,CastExpression_A,CharacterLiteral_A,ClassInstanceCreation_A,ConditionalExpression_A,CreationReference_A,ExpressionMethodReference_A,FieldAccess_A,InstanceofExpression_A,LambdaExpression_A,MethodInvocation_A,NullLiteral_A,NumberLiteral_A,ParenthesizedExpression_A,StringLiteral_A,SuperFieldAccess_A,SuperMethodInvocation_A,SuperMethodReference_A,ThisExpression_A,TypeLiteral_A,TypeMethodReference_A,VariableDeclarationExpression_A,Modifier_A,BreakStatement_A,ContinueStatement_A,DoStatement_A,ForStatement_A,IfStatement_A,ElseStatement_A,ReturnStatement_A,SuperConstructorInvocation_A,SwitchStatement_A,SwitchCase_A,SwitchDefault_A,EnhancedForStatement_A,SynchronizedStatement_A,ThrowStatement_A,TryStatement_A,WhileStatement_A,PrimitiveType_A,ArrayType_A,SimpleType_A,QualifiedType_A,WildcardType_A,ParameterizedType_A,TypeParameter_A,TIMES_A,DIVIDE_A,REMAINDER_A,PLUS_A,MINUS_A,LEFT_SHIFT_A,RIGHT_SHIFT_SIGNED_A,RIGHT_SHIFT_UNSIGNED_A,LESS_A,GREATER_A,LESS_EQUALS_A,GREATER_EQUALS_A,EQUALS_A,NOT_EQUALS_A,XOR_A,AND_A,OR_A,CONDITIONAL_AND_A,CONDITIONAL_OR_A,INCREMENT_A,DECREMENT_A,NOT_A,COMPLEMENT_A") {

      SampleGenerator.getHeader()
    }

    val sample = sg.generate()
    var count = 0

    sample.foreach(c => {

      assert(c=='0' || c=='1' || c=='2' || c==',')

      if(c=='1')
        count += 1
      else if(c=='2')
        count += 2
    })

    assert(count==12)
  }
}
