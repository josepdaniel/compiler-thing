package compiler

import ast.Expression
import ast.Const
import ast.Op1
import asm.*
import asm.Asm.*
import asm.GPRegister.*
import asm.AssemblerDefault.*

type Compiler = Expression => Seq[Asm]

val compile: Compiler = (e: Expression) => {
  e match {
    case Expression.C(c) =>
      c match {
        case Const.Integer(i) => List(Mov(rax, i))
      }
    case Expression.Prim1(Op1.Add1, e) => compile(e) :+ Add(rax, 1)
    case Expression.Prim1(Op1.Sub1, e) => compile(e) :+ Sub(rax, 1)
  }
}

val compileProgram: Compiler = (e: Expression) => {
  val prelude = List(Default(rel), Global("_entry"), Section(SectionName.text), Label("_entry"))
  val prog = compile(e)
  prelude
    ++ prog
    :+ Ret
}
