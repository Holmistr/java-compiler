import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.grammar.JavaGrammarLexer;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;
import org.github.holmistr.javacompiler.visitor.ClassVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * // TODO: Document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MainTestClass {

   public MainTestClass() throws IOException {


      // create a CharStream that reads from standard input
      InputStream is = new FileInputStream("./src/test/resources/testSources/basic_class.txt");

      ANTLRInputStream input = new ANTLRInputStream(is);
      // create a lexer that feeds off of input CharStream
      JavaGrammarLexer lexer = new JavaGrammarLexer(input);
      // create a buffer of tokens pulled from the lexer
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // create a parser that feeds off the tokens buffer
      JavaGrammarParser parser = new JavaGrammarParser(tokens);
      ParseTree tree = parser.start(); // begin parsing at init rule
      ClassVisitor visitor = new ClassVisitor();
      Class clazz = visitor.visit(tree);
      System.out.println(tree.toStringTree(parser)); // print LISP-style tree
   }

   public static void main(String[] args) throws IOException {
      new MainTestClass();
   }

}
