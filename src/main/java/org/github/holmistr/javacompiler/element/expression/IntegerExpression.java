package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class IntegerExpression extends Expression {

    private int value;

    public IntegerExpression(int value) {
        this.value = value;
        type = ExpressionType.INT;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerExpression that = (IntegerExpression) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerExpression{" +
                "value=" + value +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        type = ExpressionType.INT;
    }
}
