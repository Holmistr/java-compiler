package org.github.holmistr.javacompiler.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

/**
 * Testing that the grammar recognizes basic expressions, meaning without variables or function calls etc., just
 * constants with operators.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class BasicExpressionTest {

    @Test
    public void testInts() {
        String expression = "32";
        parseExpression(expression);

        expression = "(32)";
        parseExpression(expression);

        expression = "-32";
        parseExpression(expression);

        expression = "(-32)";
        parseExpression(expression);
    }

    @Test
    public void testFloats() {
        String expression = "32f";
        parseExpression(expression);

        expression = "-32f";
        parseExpression(expression);

        expression = "1.3432";
        parseExpression(expression);

        expression = "1.3432f";
        parseExpression(expression);

        expression = "-0.3432";
        parseExpression(expression);

        expression = ".3432f";
        parseExpression(expression);
    }

    @Test
    public void testAddition() {
        String expression = "1 + 32";
        parseExpression(expression);
    }

    @Test
    public void testSubtraction() {
        String expression = "1 - 32";
        parseExpression(expression);
    }

    @Test
    public void testMultiplication() {
        String expression = "1 * 32";
        parseExpression(expression);
    }

    @Test
    public void testDivision() {
        String expression = "1 / 32";
        parseExpression(expression);
    }

    @Test
    public void testComplexNumericExpression() {
        String expression = "(1 + (-3)) * (2 / (-1 + 2))";
        parseExpression(expression);
    }

    @Test
    public void testExpressionWithoutParenthesis() {
        String expression = "3.4f / 2 < 2 + 1";
        parseExpression(expression);
    }

    @Test
    public void testBoolean() {
        String expression = "true";
        parseExpression(expression);

        expression = "false";
        parseExpression(expression);
    }

    @Test
    public void testAnd() {
        String expression = "true && false";
        parseExpression(expression);
    }

    @Test
    public void testOr() {
        String expression = "true || false";
        parseExpression(expression);
    }

    @Test
    public void testLessThan() {
        String expression = "1 < 2";
        parseExpression(expression);
    }

    @Test
    public void testLessThanOrEqual() {
        String expression = "1 <= 2";
        parseExpression(expression);
    }

    @Test
    public void testGreaterThan() {
        String expression = "1 > 2";
        parseExpression(expression);
    }

    @Test
    public void testGreaterThanOrEqual() {
        String expression = "1 >= 2";
        parseExpression(expression);
    }

    @Test
    public void testEqual() {
        String expression = "1 == 2";
        parseExpression(expression);
    }

    @Test
    public void testNotEqual() {
        String expression = "1 != 2";
        parseExpression(expression);
    }

    @Test
    public void testNegation() {
        String expression = "!true";
        parseExpression(expression);
    }

    @Test(expected = ParseCancellationException.class)
    public void testIncorrectExpression1() {
        String expression = "1 != ";
        parseExpression(expression);
    }

    @Test(expected = ParseCancellationException.class)
    public void testIncorrectExpression2() {
        String expression = "(32 - 1.2324f";
        parseExpression(expression);
    }

    private void parseExpression(String expression) {
        ANTLRInputStream input = new ANTLRInputStream(expression);
        JavaGrammarLexer lexer = new JavaGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaGrammarParser parser = new JavaGrammarParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy()); // fail on first mismatch
        ParseTree tree = parser.expressionWithEof();
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}
