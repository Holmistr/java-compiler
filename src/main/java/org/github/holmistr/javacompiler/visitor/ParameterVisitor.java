package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.github.holmistr.javacompiler.element.Parameter;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

/**
 * Visitor for method parameters.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ParameterVisitor extends JavaGrammarBaseVisitor<Parameter> {

    @Override
    public Parameter visitParameterDeclaration(@NotNull JavaGrammarParser.ParameterDeclarationContext ctx) {
        Parameter parameter = new Parameter();
        parameter.setName(ctx.variableDeclaration().identifier().getText());
        parameter.setType(ctx.variableDeclaration().variableDeclarationType().getText());

        return parameter;
    }
}
