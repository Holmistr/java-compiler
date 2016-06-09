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
      ParseTree tree = parser.start(); // begin parsing at init rule
      System.out.println(tree.toStringTree(parser)); // print LISP-style tree
   }

   public static void main(String[] args) throws IOException {
      new MainTestClass();
   }

   /**
    * Fibonacciho posloupnost dynamicky
    * @param index poradi cisla (pocitano od 0)
    * @return Fibonacciho cislo na danem poradi
    */
   public static int fibonacci(int index){
      if(index == 0) return 0;
      if(index == 1) return 1;
      int prePre = 0; //predminule cislo
      int pre = 1; //minule cislo
      int result = 0; //vysledek
      for(int i = 1; i < index; i++){ //pocitame od druheho indexu
         result = prePre + pre; //vysledek je soucet dvou minulych cisel
         prePre = pre; //v dalsim kroku je minule predminulym
         pre = result; //a vysledek je minulym cislem
      }
      return result;
   }

}
