package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.element.Parameter;
import org.github.holmistr.javacompiler.element.statement.Statement;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor for class methods.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MethodVisitor extends JavaGrammarBaseVisitor<Method> {

    @Override
    public Method visitMethod(@NotNull JavaGrammarParser.MethodContext ctx) {
        Method method = new Method();
        method.setName(ctx.identifier().getText());

        List<AccessFlag> accessFlags = ctx.fieldAndMethodAccessModifiers().stream()
                .map(accessFlag -> AccessFlag.parse(accessFlag.getText()))
                .collect(Collectors.toList());
        method.setAccessFlags(accessFlags);

        method.setReturnType(ctx.returnType() == null ? null : ctx.returnType().getText());

        ParameterVisitor parameterVisitor = new ParameterVisitor();
        List<Parameter> parameters = ctx.parametersDeclaration().parameterDeclaration().stream()
                .map(parameter -> parameter.accept(parameterVisitor))
                .collect(Collectors.toList());
        method.setParameters(parameters);

        List<Statement> methodBody = ctx.methodBody().statements().statement().stream()
                .map(statement -> statement.accept(new StatementVisitor()))
                .collect(Collectors.toList());
        method.setBody(methodBody);

        return method;
    }
}
