package compiler

import ast.Expression
import ast.Const
import ast.Op1
import asm.*
import asm.Asm.*
import asm.GPRegister.*
import asm.AssemblerDefault.*
import ast.Expression
import scala.util.Random

type Compiler = Expression => Seq[Asm]

def generateLabel(): String = {
  "_" + Random.alphanumeric.take(15).mkString
}

val compile: Compiler = (e: Expression) => {
  e match {
    case Expression.C(c) =>
      c match {
        case Const.Integer(i) => List(Mov(rax, i))
      }
    case Expression.Prim1(Op1.Add1, e) => compile(e) :+ Add(rax, 1)
    case Expression.Prim1(Op1.Sub1, e) => compile(e) :+ Sub(rax, 1)

    // For now we can only compile ifZero type conditionals
    case Expression.Conditional(Expression.Prim1(Op1.IsZero, e0), e1, e2) =>
      val left = generateLabel()
      val right = generateLabel()
      compile(e0) ++ List(
        Cmp(rax, 0),
        Je(left)
      ) ++ compile(e2) ++ List(
        Jmp(right),
        Label(left)
      ) ++ compile(e1) :+ Label(right)
  }
}

val compileProgram: Compiler = (e: Expression) => {
  val prelude = List(Default(rel), Global("_entry"), Section(SectionName.text), Label("_entry"))
  val prog = compile(e)
  prelude
    ++ prog
    :+ Ret
}
