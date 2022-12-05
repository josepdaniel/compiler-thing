import parser.*

IntParser._parse("32")
IntParser._parse("3.2")

IntParser._parse("32")

ExpressionParser.parse("( add1 ( sub1 ( add1 2 ) ) )")
ExpressionParser.parse("3")
ExpressionParser.parse("3.2")

ExpressionParser.parse("( if  (if 0 1 3)  2 (add1 (sub1 3)))")
ExpressionParser.parse("(zero? 3)")
