package org.github.holmistr.javacompiler.grammar;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.tree.ParseTree;
import org.github.holmistr.javacompiler.util.Util;
import org.junit.Test;

/**
 * Testing that the grammar recognizes various valid Java classes, including package and import statements.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ComplexSyntaxTest {

    @Test
    public void testAdvancedStatementAndBlockTestSourceWithoutImportsAndPackage() {
        parseFile("AdvancedStatementAndBlockTestSourceWithoutImportsAndPackage.txt");
    }

    @Test
    public void testAdvancedStatementAndBlockTestSource() {
        parseFile("AdvancedStatementAndBlockTestSource.txt");
    }

    @Test
    public void testFibonnaciSource() {
        parseFile("FibonnaciSource.txt");
    }

    @Test
    public void testBasicClassSource() {
        parseFile("BasicClass.txt");
    }

    private void parseFile(String filename) {
        filename = "src/test/resources/testSources/" + filename;
        JavaGrammarParser parser = Util.createGrammarParserFromFile(filename);
        parser.setErrorHandler(new BailErrorStrategy()); // fail on first mismatch
        ParseTree tree = parser.start();
    }
}
