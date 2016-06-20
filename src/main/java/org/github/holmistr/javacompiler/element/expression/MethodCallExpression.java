package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MethodCallExpression extends Expression {

    private String name;
    private List<Expression> arguments = Collections.emptyList();

    public List<Expression> getArguments() {
        return arguments;
    }

    public void setArguments(List<Expression> arguments) {
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallExpression that = (MethodCallExpression) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return arguments != null ? arguments.equals(that.arguments) : that.arguments == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MethodCallExpression{" +
                "arguments=" + arguments +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        if (type != null) {
            return;
        }
        //TODO: implement this
    }
}
