import parser.*

IntParser._parse("32")
IntParser._parse("3.2")

IntParser._parse("32")
FloatParser._parse("3.")
FloatParser._parse("3.1")

ExpressionParser.parse("( add1 ( sub1 ( add1 2.3 ) ) )")
ExpressionParser.parse("3")
ExpressionParser.parse("3.2")
