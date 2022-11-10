package interpreter

import ast.Expression.*
import ast.Expression
import ast.Op1
import ast.Op1
import ast.Op1

def interpretProgram(e: Expression): Unit = {
  interpret(e) match {
    case Error(msg)       => println(s"Runtime rror: $msg")
    case FloatingPoint(i) => println(i)
    case Integer(i)       => println(i)
  }
}

def interpret(e: Expression): Expression = {
  e match {
    case int @ Integer(i)         => int
    case float @ FloatingPoint(f) => float
    case Prim1(op, e)             => interpretOp1(op, e)
    case error @ Error(msg)       => error
  }
}

def interpretOp1(op: Op1, e: Expression): Expression = {
  op match {
    case Op1.Add1 => interpAdd1(e)
    case Op1.Sub1 => interpSub1(e)
  }
}

def interpAdd1(e: Expression): Expression = interpret(e) match {
  case FloatingPoint(f) => FloatingPoint(f + 1.0)
  case Integer(i)       => Integer(i + 1)
  case other => Error(s"Can not apply `add1` to an object of type $other")
}

def interpSub1(e: Expression): Expression = interpret(e) match {
  case FloatingPoint(f) => FloatingPoint(f - 1.0)
  case Integer(i)       => Integer(i - 1)
  case other => Error(s"Can not apply `sub1` to an object of type $other")
}
