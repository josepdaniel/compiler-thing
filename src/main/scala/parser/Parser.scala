package parser

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
        println(acc)
        val ap = self._parse(rest)
        ap.result match {
          case Some(result) => go(acc.appended(result), ap.rest)
          case None         => (acc, rest)
        }
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

  def <*[B](b: Parser[B]): Parser[A] = new Parser[A] {
    def _parse(s: String): ParseResult[A] = {
      val ap = self._parse(s)
      ap.result match {
        case None => ap
        case Some(_) => {
          val bp = b._parse(ap.rest)
          bp.result match {
            case None    => ParseResult(None, ap.rest)
            case Some(_) => ap.copy(rest = bp.rest)
          }
        }
      }
    }
  }

  def *>[B](b: Parser[B]): Parser[B] = new Parser[B] {
    def _parse(s: String): ParseResult[B] = {
      val ap = self._parse(s)
      ap.result match {
        case Some(_) => b._parse(ap.rest)
        case None    => ParseResult(None, ap.rest)
      }
    }
  }

  def |(b: Parser[A]): Parser[A] = new Parser[A] {
    def _parse(s: String): ParseResult[A] = {
      val ap = self._parse(s)
      ap.result match {
        case None if ap.rest.length == s.length => b._parse(s)
        case _                                  => ap
      }
    }
  }
}
