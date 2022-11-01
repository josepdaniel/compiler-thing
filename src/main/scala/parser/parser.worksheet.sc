import parser.{Parser, ParseResult}

def charParser(a: Char): Parser[Char] = new Parser[Char] {
  def _parse(s: String): ParseResult[Char] =
    if s.charAt(0) == a
    then ParseResult(Some(a), s.slice(1, s.length()))
    else ParseResult(None, s)
}

val aParser = charParser('a')
val bParser = charParser('b')

val aThenBParser = aParser *> bParser
aThenBParser._parse("absolutely")
aThenBParser._parse("assolutely")

val aThenBParserButKeepA = aParser <* bParser
aThenBParserButKeepA._parse("absolutely")
aThenBParserButKeepA._parse("assolutely")
aThenBParserButKeepA.backtrack._parse("assolutely")

val aAndBParser = aParser ~ bParser
aAndBParser._parse("absolutely")
aAndBParser._parse("assolutely")

val charToNumberParser = aParser.map(c => c.toInt)
charToNumberParser.parse("awesome")

val aaaParser = aParser.rep0
aaaParser._parse("aaabsolutely")

val a1Parser = aParser.rep1
a1Parser._parse("aaabsolutely")

val aOrBParser = aParser | bParser
aOrBParser._parse("absolutely")
aOrBParser._parse("bsolutely")
