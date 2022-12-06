package ast

enum Op1 {
  case Add1
  case Sub1
  case IsZero
}

enum Const {
  case Integer(i: Int)
  case True
  case False
}

enum Expression {
  case C(c: Const)
  case Prim1(op: Op1, e: Expression)
  case Conditional(e0: Expression, e1: Expression, e2: Expression)
}

def Integer(i: Int) = Expression.C(Const.Integer(i))
