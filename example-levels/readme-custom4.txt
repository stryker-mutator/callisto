custom level 3:
removed:
  from custom level 1:
  BlockStatement
  StringLiteral
  UnaryOperator
  ObjectLiteral
  Regex
  ConditionalExpression===Tofalse
  ConditionalExpression===Totrue
  EqualityOperator===To!==

  from custom level 2:
  ConditionalExpressionEmptyCase
  ConditionalExpressionConditionTofalse
  ConditionalExpressionConditionTotrue
  BooleanLiteralRemoveNegation

  from custom level 3:
  ArrayDeclarationEmpty
  ArrayDeclarationEmptyConstructor
  ArrayDeclarationFill
  ArrowFunction

  additional removals:
  LogicalOperator||To&&
  LogicalOperator&&To||

