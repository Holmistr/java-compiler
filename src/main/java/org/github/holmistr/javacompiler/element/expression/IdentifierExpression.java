package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class IdentifierExpression extends Expression {

    private String value;

    public IdentifierExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentifierExpression atom = (IdentifierExpression) o;

        return value != null ? value.equals(atom.value) : atom.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "IdentifierExpression{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        Variable variable = variables.get(value);
        if (variable == null) {
            throw new IllegalStateException("Variable '" + value + "' is not defined.");
        }

        type = variable.getType();
    }
}
