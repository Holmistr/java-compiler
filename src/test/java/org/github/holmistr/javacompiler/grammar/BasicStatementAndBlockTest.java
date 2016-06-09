package org.github.holmistr.javacompiler.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing that the grammar recognizes basic blocks and statements, like for, while, if, class etc.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class BasicStatementAndBlockTest {

    @Test
    public void testVariableDeclaration() {
        String statement = "int i = 21;";
        parseStatement(statement);

        statement = "String string = new String(\"Hello\");";
        parseStatement(statement);

        statement = "String string[][] = new String[10][10];";
        parseStatement(statement);

        statement = "String string = \"Hello world\";";
        parseStatement(statement);
    }

    @Test
    public void testFor() {
        String statement = "for (int i = 1; i < 100; i++) System.out.println(\"Hello\");";
        parseStatement(statement);

        statement = "for (int i = 0; i < 100; i++) { System.out.println(\"Hello\"); System.out.println(\"World!\"); }";
        parseStatement(statement);
    }

    @Test
    public void testWhile() {
        String statement = "while ((line = reader.readLine()) != null) System.out.println(\"Hello\");";
        parseStatement(statement);

        statement = "while (true) { System.out.println(\"Hello\"); System.out.println(\"World!\"); }";
        parseStatement(statement);
    }

    @Test
    public void testIf() {
        String statement = "if (i < 0) System.out.println(\"Hello\");";
        parseStatement(statement);

        statement = "if (i < 0) { System.out.println(\"Hello\"); System.out.println(\"World!\"); }";
        parseStatement(statement);
    }

    @Test
    public void testIfElse() {
        String statement = "if (i < 0) System.out.println(\"Hello\"); else System.out.println(\"Nooooo!\");";
        parseStatement(statement);

        statement = "if (i < 0) { System.out.println(\"Hello\"); System.out.println(\"World!\"); } else { System.out.println(\"Noooo!\"); }";
        parseStatement(statement);
    }

    @Test
    public void testIfElseIfElse() {
        String statement = "if (i < 0) System.out.println(\"Hello\"); else if (i > 0) System.out.println(\"What?\") else System.out.println(\"Nooooo!\");";
        parseStatement(statement);

        statement = "if (i < 0) { System.out.println(\"Hello\"); System.out.println(\"World!\"); } if (i > 0) { System.out.println(\"What?\"); System.out.println(\"World!\"); } else { System.out.println(\"Noooo!\"); }";
        parseStatement(statement);
    }

    @Test
    public void testBreak() {
        String statement = "for (int i = 1; i < 100; i++) break;";
        parseStatement(statement);

        statement = "while (true) { break; }";
        parseStatement(statement);
    }

    @Test
    public void testContinue() {
        String statement = "for (int i = 1; i < 100; i++) continue;";
        parseStatement(statement);

        statement = "while (true) { continue; }";
        parseStatement(statement);
    }

    @Test
    public void testSwitch() {
        String statement = "switch (variable) { case 1: call(); break; case 2: call(); break; default: call(); }";
        parseStatement(statement);

        statement = "switch (variable) { case 1: call(); break; case 2: call(); }";
        parseStatement(statement);
    }

    @Test
    public void testReturn() {
        String statement = "return;";
        parseStatement(statement);

        statement = "return variable;";
        parseStatement(statement);
    }

    private void parseStatement(String statement) {
        ANTLRInputStream input = new ANTLRInputStream(statement);
        JavaGrammarLexer lexer = new JavaGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaGrammarParser parser = new JavaGrammarParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy()); // fail on first mismatch
        ParseTree tree = parser.statementWithEof();
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
