package interpreter

import ast.Expression
import ast.Expression.*
import ast.Const.*
import ast.Op1

def interpretProgram(e: Expression): Unit = {
  interpret(e) match {
    case C(c) =>
      c match {
        case Integer(i) => println(i)
      }
    case other => println(s"Something went wrong yo")
  }
}

def interpret(e: Expression): Expression = {
  e match {
    case int @ C(Integer(i)) => int
    case Prim1(op, e)        => interpretOp1(op, e)
  }
}

def interpretOp1(op: Op1, e: Expression): Expression = {
  op match {
    case Op1.Add1 => interpAdd1(e)
    case Op1.Sub1 => interpSub1(e)
  }
}

def interpAdd1(e: Expression): Expression = interpret(e) match {
  case C(Integer(i)) => C(Integer(i + 1))
}

def interpSub1(e: Expression): Expression = interpret(e) match {
  case C(Integer(i)) => C(Integer(i - 1))
}
