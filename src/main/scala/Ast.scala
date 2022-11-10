package ast

enum Op1 {
  case Add1
  case Sub1
}

enum Expression {
  case Integer(i: Int)
  case FloatingPoint(i: Double)
  case Prim1(op: Op1, e: Expression)
  case Error(msg: String)
}
