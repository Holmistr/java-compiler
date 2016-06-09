package org.github.holmistr.javacompiler.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing that the grammar recognizes more complicated expressions like expressions with variables, arrays,
 * function calls, etc.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class AdvancedExpressionTest {

    @Test
    public void testCorrectVariable() {
        String expression = "myVariable";
        parseExpression(expression);

        expression = "$22Jj_";
        parseExpression(expression);

        expression = "_1";
        parseExpression(expression);
    }

    @Test(expected = ParseCancellationException.class)
    public void testIncorrectVariable1() {
        String expression = "1kd";
        parseExpression(expression);
    }

    @Test(expected = ParseCancellationException.class)
    public void testIncorrectVariable2() {
        String expression = "var!able";
        parseExpression(expression);
    }

    @Test
    public void testExpressionWithVariables() {
        String expression = "(x / radius) * (2 + circleDiameter)";
        parseExpression(expression);
    }

    @Test
    public void testArray() {
        String expression = "array[2]";
        parseExpression(expression);

        expression = "array[1]";
        parseExpression(expression);

        expression = "array[i+1]";
        parseExpression(expression);
    }

    @Test
    public void testMethodCall() {
        String expression = "object.toString()";
        parseExpression(expression);

        expression = "object.toString(argument1, argument2)";
        parseExpression(expression);
    }

    @Test
    public void testAloneMethodCall() {
        String expression = "print(\"something\")";
        parseExpression(expression);
    }

    @Test
    public void testChainedMethodCall() {
        String expression = "System.out.println()";
        parseExpression(expression);

        expression = "first.second.thirdMethod.call(\"haha\")";
        parseExpression(expression);
    }

    @Test
    public void testNewConstruct() {
        String expression = "new String()";
        parseExpression(expression);

        expression = "new String(argument1, argument2)";
        parseExpression(expression);

        expression = "new String[10]";
        parseExpression(expression);
    }

    @Test
    public void testAssignment() {
        String expression = "myVariable = 2";
        parseExpression(expression);

        expression = "myVariable[1] = 2";
        parseExpression(expression);
    }

    @Test
    public void testStringConstruct() {
        String expression = "\"My String\"";
        parseExpression(expression);

        expression = "\"My String \\\" with quotes \\\" \"";
        parseExpression(expression);
    }

    @Test
    public void testCasting() {
        String expression = "(int) 1f";
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
