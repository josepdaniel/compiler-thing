package ast

enum Op1 {
  case Add1
  case Sub1
}

enum Const {
  case Integer(i: Int)
}

enum Expression {
  case C(c: Const)
  case Prim1(op: Op1, e: Expression)
}

def Integer(i: Int) = Expression.C(Const.Integer(i))
