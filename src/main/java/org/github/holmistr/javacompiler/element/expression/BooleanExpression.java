package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class BooleanExpression extends Expression {

    private boolean value;

    public BooleanExpression(boolean value) {
        this.value = value;
        type = ExpressionType.BOOLEAN;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooleanExpression that = (BooleanExpression) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public String toString() {
        return "BooleanExpression{" +
                "value=" + value +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        type = ExpressionType.BOOLEAN;
    }
}
