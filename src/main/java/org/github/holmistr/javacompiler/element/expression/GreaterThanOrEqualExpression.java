package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class GreaterThanOrEqualExpression extends Expression {

    private Expression leftOperand;
    private Expression rightOperand;

    public GreaterThanOrEqualExpression(Expression leftOperand, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GreaterThanOrEqualExpression that = (GreaterThanOrEqualExpression) o;

        if (leftOperand != null ? !leftOperand.equals(that.leftOperand) : that.leftOperand != null) return false;
        return rightOperand != null ? rightOperand.equals(that.rightOperand) : that.rightOperand == null;
    }

    @Override
    public int hashCode() {
        int result = leftOperand != null ? leftOperand.hashCode() : 0;
        result = 31 * result + (rightOperand != null ? rightOperand.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GreaterThanOrEqualExpression{" +
                "leftOperand=" + leftOperand +
                ", rightOperand=" + rightOperand +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        if (type != null) {
            return;
        }

        if (leftOperand.getType() == null) {
            leftOperand.resolveType(variables);
        }

        if (rightOperand.getType() == null) {
            rightOperand.resolveType(variables);
        }

        if (leftOperand.getType() == ExpressionType.INT && rightOperand.getType() == ExpressionType.INT) {
            type = ExpressionType.BOOLEAN;
        } else {
            throw new IllegalStateException("Operator '>=' must be used on int.");
        }
    }
}
