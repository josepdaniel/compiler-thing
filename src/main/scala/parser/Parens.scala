package parser

def parens[A](p: Parser[A]): Parser[A] =
  Parser.charParser(_ == '(') *> p <* Parser.charParser(_ == ')')
