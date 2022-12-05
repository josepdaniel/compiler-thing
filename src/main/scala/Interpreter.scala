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
        case True       => println("#t")
        case False      => println("#f")
      }
    case other => println(s"Something went wrong yo")
  }
}

def interpret(e: Expression): Expression = {
  e match {
    case int @ C(Integer(i))      => int
    case b @ (C(True) | C(False)) => b
    case Prim1(op, e)             => interpretOp1(op, e)
    case IfZero(e0, e1, e2)       => interpConditional(e0, e1, e2)
  }
}

def interpretOp1(op: Op1, e: Expression): Expression = {
  op match {
    case Op1.Add1   => interpAdd1(e)
    case Op1.Sub1   => interpSub1(e)
    case Op1.IsZero => interpIsZero(e)
  }
}

def interpAdd1(e: Expression): Expression = interpret(e) match {
  case C(Integer(i)) => C(Integer(i + 1))
  case other         => throw (Exception(s"Can not add1 to $other"))
}

def interpSub1(e: Expression): Expression = interpret(e) match {
  case C(Integer(i)) => C(Integer(i - 1))
  case other         => throw (Exception(s"Can not sub1 from $other"))
}

def interpIsZero(e: Expression): Expression = interpret(e) match {
  case C(Integer(0)) => C(True)
  case _             => C(False)
}

def interpConditional(e0: Expression, e1: Expression, e2: Expression) = {
  interpret(e0) match {
    case C(True) => interpret(e1)
    case _       => interpret(e2)
  }
}
