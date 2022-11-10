package parser
import Parser.*

val Add1Parser: Parser[ast.Op1] = `match`("add1").map(_ => ast.Op1.Add1)
val Sub1Parser: Parser[ast.Op1] = `match`("sub1").map(_ => ast.Op1.Sub1)

val Op1Parser: Parser[ast.Op1] = Add1Parser | Sub1Parser

def Op1ApplicationParser(
    recurse: Parser[ast.Expression]
): Parser[ast.Expression] =
  val lhs = whitespaces0 *> Op1Parser <* whitespaces1
  val rhs = recurse <* whitespaces0
  parens(lhs ~ rhs).map((op, exp) => ast.Expression.Prim1(op, exp))
