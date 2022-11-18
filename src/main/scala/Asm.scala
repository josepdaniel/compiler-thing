package asm

enum GPRegister {
  case rax
  case rcx
  case rdx
  case rbx
  case rsp
  case rbp
  case rsi
  case rdi
}

enum AssemblerDefault {
  case rel
  case sbs
  case bnd
  case nobnd
}

enum SectionName {
  case text
  case data
}

enum Asm(val str: String) {
  case Default(assemblerDefault: AssemblerDefault) extends Asm(s"\tdefault ${assemblerDefault.toString}")
  case Section(name: SectionName) extends Asm(s"\tsection .${name.toString}")
  case Global(label: String) extends Asm(s"\tglobal ${label}")
  case Label(label: String) extends Asm(s"${label}:")
  case Mov(dst: GPRegister, src: Long) extends Asm(s"\tmov ${dst.toString()} ${src}")
  case Ret extends Asm("\tret")
}
