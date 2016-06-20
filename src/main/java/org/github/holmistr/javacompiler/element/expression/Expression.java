package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.element.statement.Statement;
import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public abstract class Expression extends Statement {

    protected ExpressionType type;

    public ExpressionType getType() {
        return type;
    }

    public abstract void resolveType(Map<String, Variable> variables);
}
