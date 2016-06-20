package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.tree.ParseTree;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.element.Parameter;
import org.github.holmistr.javacompiler.element.expression.IdentifierExpression;
import org.github.holmistr.javacompiler.element.expression.MethodCallExpression;
import org.github.holmistr.javacompiler.element.expression.StringExpression;
import org.github.holmistr.javacompiler.element.expression.ChainExpression;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;
import org.github.holmistr.javacompiler.util.Util;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests for functionality of the visitor - if the source file is mapped correctly to the entities.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class VisitorMappingTest {

    @Test
    public void testBasicClass() {
        Class parsedClass = visitSourceFile("BasicClass.txt");
        Class expectedClass = createBasicClassTestMapping();

        assertEquals(expectedClass, parsedClass);
    }

    private Class createBasicClassTestMapping() {
        Class clazz = new Class();
        clazz.setThisClass("BasicClass");
        clazz.setAccessFlags(Arrays.asList(AccessFlag.SUPER, AccessFlag.PUBLIC));

        Method mainMethod = new Method();
        mainMethod.setName("main");
        mainMethod.setReturnType("void");
        mainMethod.setAccessFlags(Arrays.asList(AccessFlag.PUBLIC, AccessFlag.STATIC));

        Parameter parameter = new Parameter();
        parameter.setName("args");
        parameter.setType("String[]");
        mainMethod.setParameters(Arrays.asList(parameter));

        IdentifierExpression statement1 = new IdentifierExpression("System");
        IdentifierExpression statement2 = new IdentifierExpression("out");

        MethodCallExpression statement3 = new MethodCallExpression();
        statement3.setName("println");
        StringExpression expression = new StringExpression("Hello world!");
        statement3.setArguments(Arrays.asList(expression));

        ChainExpression chain = new ChainExpression();
        chain.setChain(Arrays.asList(statement1, statement2, statement3));

        mainMethod.setBody(Arrays.asList(chain));
        clazz.setMethods(Arrays.asList(mainMethod));

        return clazz;
    }

    private Class visitSourceFile(String filename) {
        filename = "src/test/resources/testSources/" + filename;
        JavaGrammarParser parser = Util.createGrammarParserFromFile(filename);
        ParseTree tree = parser.start(); // begin parsing at init rule
        ClassVisitor visitor = new ClassVisitor();
        Class clazz = visitor.visit(tree);

        return clazz;
    }
}
