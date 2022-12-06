package parser

import Parser.*

/** Matches expressions of the form `(if e0 e1 e2)`
  */
def ConditionalParser(recurse: Parser[ast.Expression]): Parser[ast.Expression] =
  val IfExpr = `match`("if").void
  parens(
    (whitespaces0 *> IfExpr *> whitespaces1) *> (recurse <* whitespaces1) ~ (recurse <* whitespaces1) ~ (recurse <* whitespaces0)
  ).map { case ((e0, e1), e2) => ast.Expression.Conditional(e0, e1, e2) }
