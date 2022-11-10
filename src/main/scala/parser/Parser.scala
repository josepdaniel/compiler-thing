package parser

import scala.util.Try

case class ParseResult[A](result: Option[A], rest: String)

/** A small parser inspired by cats-parse
  */
trait Parser[A] { self =>
  def _parse(s: String): ParseResult[A]

  def parse(s: String): Option[A] = _parse(s).result

  def map[B](f: A => B): Parser[B] = new Parser[B] {
    def _parse(s: String): ParseResult[B] =
      val ap = self._parse(s)
      ParseResult(ap.result.map(f), ap.rest)
  }

  def rep1: Parser[List[A]] = new Parser[List[A]] {
    def _parse(s: String): ParseResult[List[A]] =
      val ap = self.rep0._parse(s)
      ap.result match {
        case Some(result) =>
          if result.length > 0 then ap else ParseResult(None, ap.rest)
        case None => ap
      }

  }

  def rep0: Parser[List[A]] = new Parser[List[A]] {

    def _parse(s: String): ParseResult[List[A]] =

      def go(acc: List[A], rest: String): (List[A], String) = {
        if rest.length > 0 then {
          val ap = self._parse(rest)
          ap.result match {
            case Some(result) => go(acc.appended(result), ap.rest)
            case None         => (acc, rest)
          }
        } else (acc, rest)

      }

      val result = go(List.empty[A], s)
      ParseResult(Some(result._1), result._2)

  }

  def backtrack = new Parser[A] {
    def _parse(s: String): ParseResult[A] = {
      val ap = self._parse(s)
      ap.result match {
        case Some(_) => ap
        case None    => ParseResult(None, s)
      }
    }
  }

  def ~[B](b: Parser[B]): Parser[(A, B)] = new Parser[(A, B)] {
    def _parse(s: String): ParseResult[(A, B)] =
      val ap = self._parse(s)
      ap.result match {
        case None => ParseResult(None, s)
        case Some(resultA) => {
          val bp = b._parse(ap.rest)
          bp.result match {
            case None          => ParseResult(None, ap.rest)
            case Some(resultB) => ParseResult(Some((resultA, resultB)), bp.rest)
          }
        }
      }
  }

  def <*[B](b: Parser[B]): Parser[A] =
    (self ~ b.void).map(_._1)

  def *>[B](b: Parser[B]): Parser[B] =
    (self.void ~ b).map(_._2)

  def |(b: Parser[A]): Parser[A] = new Parser[A] {
    def _parse(s: String): ParseResult[A] = {
      val ap = self._parse(s)
      ap.result match {
        case None if ap.rest.length == s.length => b._parse(s)
        case _                                  => ap
      }
    }
  }

  def `try`[B](f: A => Option[B]): Parser[B] = self.map(f).flatten

  def void: Parser[Unit] = self.map(_ => ())

}

object Parser {

  extension [A](p: Parser[Option[A]]) {
    def flatten: Parser[A] = new Parser[A]:
      override def _parse(s: String): ParseResult[A] = {
        val ap = p._parse(s)
        ap.result match {
          case Some(Some(result)) => ParseResult(Some(result), ap.rest)
          case _                  => ParseResult(None, s)
        }
      }
  }

  def charParser(f: Char => Boolean) = new Parser[Char] {
    def _parse(s: String): ParseResult[Char] =
      if s.length() == 0 then ParseResult(None, s)
      else {
        val char = s.charAt(0)
        if (f(char))
        then ParseResult(Some(char), s.slice(1, s.length))
        else ParseResult(None, s)
      }
  }

  def `match`(matches: String): Parser[String] = new Parser[String] {
    def _parse(s: String): ParseResult[String] = {
      if matches.length() == 0 then {
        ParseResult(Some(""), s)
      } else if s.length() == 0 then {
        ParseResult(None, s)
      } else if s.charAt(0) == matches.charAt(0) &&
        `match`(matches.slice(1, matches.length))
          .parse(s.slice(1, s.length()))
          .isDefined
      then {
        ParseResult(Some(matches), s.slice(matches.length(), s.length()))
      } else {
        ParseResult(None, s)
      }
    }
  }

  val eof = new Parser[Unit] {
    def _parse(s: String): ParseResult[Unit] =
      if s.length() == 0
      then ParseResult(Some(()), "")
      else ParseResult(None, s)
  }

  def parseWhile(f: Char => Boolean): Parser[String] =
    charParser(f).rep0.map(cs => cs.mkString)

  def parseUntil(f: Char => Boolean): Parser[String] =
    charParser(c => !f(c)).rep0.map(cs => cs.mkString)
      <* (charParser(f).void | eof)

  def `lazy`[A](f: () => Parser[A]): Parser[A] = new Parser[A] {
    override def _parse(s: String): ParseResult[A] = {
      f()._parse(s)
    }
  }
  // todo: Make this function also accept recursive parsers
  def recurse[A](f: Parser[A] => Parser[A]) = {
    lazy val result: Parser[A] = f(`lazy`(() => result))
    result
  }

  val whitespace: Parser[Unit] = charParser(_.isWhitespace).void
  val whitespaces0: Parser[Unit] = whitespace.rep0.void
  val whitespaces1: Parser[Unit] = whitespace.rep1.void

  val alphanumericString: Parser[String] = parseWhile(_.isLetterOrDigit)
  val alphabeticString: Parser[String] = parseWhile(_.isLetter)
  val numericString: Parser[String] = parseWhile(_.isDigit)
  val floatingPointNumericString: Parser[String] =
    (numericString ~ Parser.charParser(_ == '.') ~ numericString).map(out =>
      out._1._1 + out._1._2 + out._2
    )

}
