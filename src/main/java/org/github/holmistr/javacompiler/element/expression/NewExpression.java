package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class NewExpression extends Expression {

    private String className;
    private List<Expression> arguments;

    public NewExpression(String className, List<Expression> arguments) {
        this.className = className;
        this.arguments = arguments;
        type = ExpressionType.REFERENCE;
    }

    public String getClassName() {
        return className;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        type = ExpressionType.REFERENCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewExpression that = (NewExpression) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        return arguments != null ? arguments.equals(that.arguments) : that.arguments == null;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewExpression{" +
                "arguments=" + arguments +
                ", className='" + className + '\'' +
                '}';
    }
}
