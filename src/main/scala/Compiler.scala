package compiler

import ast.Expression
import ast.Const
import asm.*
import asm.Asm.*
import asm.GPRegister.*
import asm.AssemblerDefault.*

type Compiler = Expression => Seq[Asm]

val compile: Compiler = (e: Expression) => {
  val prog = e match {
    case Expression.C(c) =>
      c match {
        case Const.Integer(i) => List(Mov(rax, i))
      }
  }

  val prelude = List(
    Default(rel),
    Global("_entry"),
    Section(SectionName.text),
    Label("_entry")
  )

  prelude
    ++ prog
    :+ Ret
}

def compileToString(e: Expression) = {
  compile(e).map(_.str).mkString("\n")
}
