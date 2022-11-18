package ast

enum Op1 {
  case Add1
  case Sub1
}

enum Const {
  case Integer(i: Int)
  case FloatingPoint(i: Double)
  case Error(msg: String)
}

enum Expression {
  case C(c: Const)
  case Prim1(op: Op1, e: Expression)
}

def Integer(i: Int) = Expression.C(Const.Integer(i))
def FloatingPoint(f: Double) = Expression.C(Const.FloatingPoint(f))
