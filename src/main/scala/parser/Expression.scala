package parser

val ExpressionParser = Parser.recurse[ast.Expression] { r =>
  IntParser | Op1ApplicationParser(r) | ConditionalParser(r)
}
