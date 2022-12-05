import interpreter.*
import parser.*

interpretProgram(ExpressionParser.parse("(add1 (add1 (add1 2)))").get)
interpretProgram(ExpressionParser.parse("(if (zero? (sub1 ( sub1 2))) 1 3)").get)
