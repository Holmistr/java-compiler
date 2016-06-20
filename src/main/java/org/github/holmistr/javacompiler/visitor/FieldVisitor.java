package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Field;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor for class fields.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class FieldVisitor extends JavaGrammarBaseVisitor<Field> {

    @Override
    public Field visitField(@NotNull JavaGrammarParser.FieldContext ctx) {
        Field field = new Field();

        List<AccessFlag> accessFlags = ctx.fieldAndMethodAccessModifiers().stream()
                .map(accessFlag -> AccessFlag.parse(accessFlag.getText()))
                .collect(Collectors.toList());
        field.setAccessFlags(accessFlags);

        field.setType(ctx.variableDeclarationWithInitialization().variableDeclaration().variableDeclarationType().getText());
        field.setName(ctx.variableDeclarationWithInitialization().variableDeclaration().identifier().getText());

        return field;
    }
}
