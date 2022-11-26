import interpreter.*
import parser.*

interpretProgram(ExpressionParser.parse("(add1 (add1 (add1 2.3)))").get)
