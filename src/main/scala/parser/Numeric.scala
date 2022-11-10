package parser
import scala.util.Try
import Parser.*
import ast.*
import scala.quoted.Expr

val IntParser = numericString.`try`(_.toIntOption).map(Expression.Integer.apply)

val FloatParser = floatingPointNumericString
  .`try`(_.toDoubleOption)
  .map(Expression.FloatingPoint.apply)
