package org.github.holmistr.javacompiler.grammar;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.tree.ParseTree;
import org.github.holmistr.javacompiler.util.Util;
import org.junit.Test;

/**
 * Testing that the grammar recognizes advanced blocks and statements, like classes, methods etc.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class AdvancedStatementAndBlockTest {

    @Test
    public void testEmptyClass() {
        String clazz = "class MyClass {}";
        parseClass(clazz);

        clazz = "public class MyClass {}";
        parseClass(clazz);
    }

    @Test
    public void testConstructor() {
        String clazz = "class MyClass {" +
                "public MyClass() {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testMainMethod() {
        String clazz = "class MyClass {" +
                "public static void main(String[] args) {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testMethod() {
        String clazz = "class MyClass {" +
                "public void method1() {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testMethodWithParams() {
        String clazz = "class MyClass {" +
                "public void method1(String param1, int param2) {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testMethodNonVoidMethod() {
        String clazz = "class MyClass {" +
                "public String method1() {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testPrivateMethod() {
        String clazz = "class MyClass {" +
                "private void method1(String param1, int param2) {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testDefaultMethod() {
        String clazz = "class MyClass {" +
                "void method1() {}" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testField() {
        String clazz = "class MyClass {" +
                "int x;" +
                "String y;" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testPrivateField() {
        String clazz = "class MyClass {" +
                "private int x;" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testPublicField() {
        String clazz = "class MyClass {" +
                "public int x;" +
                "}";
        parseClass(clazz);
    }

    @Test
    public void testFieldWithDeclaration() {
        String clazz = "class MyClass {" +
                "private int x = 10;" +
                "}";
        parseClass(clazz);
    }

    private void parseClass(String statement) {
        JavaGrammarParser parser = Util.createGrammarParserFromString(statement);
        parser.setErrorHandler(new BailErrorStrategy()); // fail on first mismatch
        ParseTree tree = parser.clazzWithEof();
    }
}
