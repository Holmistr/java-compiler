package org.github.holmistr.javacompiler.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Testing that the grammar recognizes various valid Java classes, including package and import statements.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ComplexSyntaxTest {

    @Test
    public void testAdvancedStatementAndBlockTestSourceWithoutImportsAndPackage() {
        parseFile("advanced_statement_and_block_test_source_without_imports_and_package.txt");
    }

    @Test
    public void testAdvancedStatementAndBlockTestSource() {
        parseFile("advanced_statement_and_block_test_source.txt");
    }

    @Test
    public void testFibonnacitSource() {
        parseFile("fibonnaci_source.txt");
    }

    private void parseFile(String filename) {
        filename = "src/test/resources/testSources/" + filename;
        ANTLRInputStream input = null;
        try {
            FileInputStream inputStream = new FileInputStream(filename);
            input = new ANTLRInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JavaGrammarLexer lexer = new JavaGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaGrammarParser parser = new JavaGrammarParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy()); // fail on first mismatch
        ParseTree tree = parser.start();
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
