import parser.*

IntParser._parse("32")
IntParser._parse("3.2")

IntParser._parse("32")

ExpressionParser.parse("( add1 ( sub1 ( add1 2 ) ) )")
ExpressionParser.parse("3")
ExpressionParser.parse("3.2")

ExpressionParser.parse("( if  (zero? 0)  2 (add1 (sub1 3)))")
ExpressionParser.parse("(zero? 3)")
ExpressionParser.parse("(if (zero? (sub1 2000000000)) (add1 2) (sub1 12))")
