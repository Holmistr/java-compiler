package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class StringExpression extends Expression {

    private String value;

    public StringExpression(String value) {
        this.value = value;
        type = ExpressionType.STRING;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringExpression that = (StringExpression) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "StringExpression{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        type = ExpressionType.STRING;
    }
}
