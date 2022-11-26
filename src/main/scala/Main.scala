import java.nio.file.Path
import scala.io.Source
import parser.ExpressionParser
import compiler.compileProgram
import java.io.BufferedWriter
import java.io.Writer
import java.io.FileWriter
import java.nio.file.FileSystems

def compileInputFile(inputFile: Path, outputFile: Path) = {
  val in = Source.fromFile(inputFile.toFile()).getLines().mkString("\n")
  ExpressionParser.parse(in) match {
    case None => System.err.println("Invalid program")
    case Some(e) => {
      val compiledProgram = compileProgram(e)
      val outputProgram = compiledProgram.map(_.str).mkString("\n")
      val writer = BufferedWriter(FileWriter.apply(outputFile.toFile()))
      writer.write(outputProgram)
      writer.close()
    }
  }
}

@main def main(inputFile: String, outputFile: String) = {
  val in = FileSystems.getDefault().getPath(inputFile);
  val out = FileSystems.getDefault().getPath(outputFile);
  compileInputFile(in, out)
}
