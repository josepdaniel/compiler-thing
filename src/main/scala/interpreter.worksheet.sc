import interpreter.*
import parser.*

val ast = ExpressionParser.parse("(add1 (add1 (add1 2.3)))").get
val interped = interpretProgram(ast)
