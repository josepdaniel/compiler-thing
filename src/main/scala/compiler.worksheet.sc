import ast.*
import compiler.*
import parser.ExpressionParser

print(compileProgram(Integer(22)))
print(compileProgram(ExpressionParser.parse("( if  (zero? 1)  2 (add1 (sub1 3)))").get))
