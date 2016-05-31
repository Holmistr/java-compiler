import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.github.holmistr.javacompiler.grammar.JavaGrammarLexer;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

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
      InputStream is = new FileInputStream("./src/main/java/test.txt");

      ANTLRInputStream input = new ANTLRInputStream(is);
// create a lexer that feeds off of input CharStream
      JavaGrammarLexer lexer = new JavaGrammarLexer(input);
// create a buffer of tokens pulled from the lexer
      CommonTokenStream tokens = new CommonTokenStream(lexer);
// create a parser that feeds off the tokens buffer
      JavaGrammarParser parser = new JavaGrammarParser(tokens);
      ParseTree tree = parser.init(); // begin parsing at init rule
      System.out.println(tree.toStringTree(parser)); // print LISP-style tree
   }

   public static void main(String[] args) throws IOException {
      new MainTestClass();
   }
}
