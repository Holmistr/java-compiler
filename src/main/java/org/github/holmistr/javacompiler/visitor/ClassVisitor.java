package org.github.holmistr.javacompiler.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.Field;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.grammar.JavaGrammarBaseVisitor;
import org.github.holmistr.javacompiler.grammar.JavaGrammarParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Visitor for whole class, basic enter point.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ClassVisitor extends JavaGrammarBaseVisitor<Class> {

    @Override
    public Class visitClazz(@NotNull JavaGrammarParser.ClazzContext ctx) {
        Class clazz = new Class();
        String className = ctx.identifier().IDENTIFIER().getText();
        clazz.setThisClass(className);

        List<AccessFlag> accessFlags = new ArrayList<>();
        accessFlags.add(AccessFlag.SUPER);
        if (ctx.clazzAccessModifiers() != null) {
            accessFlags.add(ctx.clazzAccessModifiers().accept(new ClassAccessModifierVisitor()));
        }
        clazz.setAccessFlags(accessFlags);

        if (ctx.field() != null) {
            FieldVisitor fieldVisitor = new FieldVisitor();
            List<Field> fields = ctx.field().stream()
                    .map(field -> field.accept(fieldVisitor))
                    .collect(Collectors.toList());
            clazz.setFields(fields);
        }

        MethodVisitor methodVisitor = new MethodVisitor();
        List<Method> methods = ctx.method().stream()
                .map(method -> method.accept(methodVisitor))
                .collect(Collectors.toList());
        clazz.setMethods(methods);


        return clazz;
    }
}
