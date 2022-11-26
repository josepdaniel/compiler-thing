package parser
import scala.util.Try
import Parser.*
import ast.Expression
import ast.Const

val IntParser: Parser[Expression] =
  numericString.`try`(_.toIntOption).map(i => Expression.C(Const.Integer(i)))
