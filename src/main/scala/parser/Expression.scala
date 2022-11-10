package parser

val ExpressionParser = Parser.recurse[ast.Expression] { r =>
  FloatParser | IntParser | Op1ApplicationParser(r)
}
