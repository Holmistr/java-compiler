package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

/**
 * Visitor for class access modificators.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ClassAccessModifierVisitor extends JavaGrammarBaseVisitor<AccessFlag> {

    @Override
    public AccessFlag visitClazzAccessModifiers(@NotNull JavaGrammarParser.ClazzAccessModifiersContext ctx) {
        return AccessFlag.parse(ctx.getText());
    }
}
