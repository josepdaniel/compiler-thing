import interpreter.*
import parser.*

interpretProgram(ExpressionParser.parse("(add1 (add1 (add1 2)))").get)
interpretProgram(ExpressionParser.parse("(if 0 1 3)").get)
