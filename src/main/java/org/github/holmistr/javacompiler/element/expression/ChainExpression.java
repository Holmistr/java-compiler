package org.github.holmistr.javacompiler.element.expression;

import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;
import org.github.holmistr.javacompiler.util.TypeUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ChainExpression extends Expression {

    private List<Expression> chain;

    private String typeClassName;

    public List<Expression> getChain() {
        return chain;
    }

    public void setChain(List<Expression> chain) {
        this.chain = chain;
    }

    public String getTypeClassName() {
        return typeClassName;
    }

    public void setTypeClassName(String typeClassName) {
        this.typeClassName = typeClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChainExpression chain1 = (ChainExpression) o;

        return chain != null ? chain.equals(chain1.chain) : chain1.chain == null;
    }

    @Override
    public int hashCode() {
        return chain != null ? chain.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChainExpression{" +
                "chain=" + chain +
                '}';
    }

    @Override
    public void resolveType(Map<String, Variable> variables) {
        String caller = ((IdentifierExpression) chain.get(0)).getValue();

        try {
            Class clazz = Class.forName(TypeUtil.getFullNameInDotFormat(caller));
        } catch (RuntimeException | ClassNotFoundException e) { //it's not class name (ergo static call or static field access, it must be variable access
            // load the reference and store the class name as caller
            Variable variable = variables.get(caller);
            if (variable == null) {
                throw new IllegalStateException("Variable '" + caller +"' is not defined.");
            }

            if (variable.getType() != ExpressionType.REFERENCE) {
                throw new IllegalStateException("Cannot call methods on primitive type. Variable: " + caller);
            }

            caller = TypeUtil.getFullNameInDotFormat(variable.getClassName());
        }

        for (int i = 1; i < chain.size(); i++) {
            Class clazz = null;
            try {
                clazz = Class.forName(TypeUtil.getFullNameInDotFormat(caller));
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //TODO: handle this properly
            }

            Expression tmp = chain.get(i);
            if (tmp instanceof IdentifierExpression) { //field access
                String fieldName = ((IdentifierExpression) tmp).getValue();
                Field field;
                try {
                    field = clazz.getField(fieldName);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e); //TODO: handle this properly
                }

                caller = field.getType().getCanonicalName();
            } else if (tmp instanceof MethodCallExpression) { //method call
                MethodCallExpression methodCall = (MethodCallExpression) tmp;
                for (Expression expression: methodCall.getArguments()) {
                    expression.resolveType(variables);
                }

                Class methodClass = null;
                try {
                    methodClass = Class.forName(TypeUtil.getFullNameInDotFormat(caller));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                java.lang.reflect.Method classMethod = null;
                try {
                    classMethod = methodClass.getMethod(methodCall.getName(), TypeUtil.getMethodParameterClasses(variables, methodCall.getArguments()));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                caller = classMethod.getReturnType().getCanonicalName();
            }
        }

        Class clazz = null;
        try {
            clazz = Class.forName(TypeUtil.getFullNameInDotFormat(caller));
        } catch (RuntimeException | ClassNotFoundException e) { //primitive type
            type = ExpressionType.parse(caller);
            return;
        }

        type = ExpressionType.REFERENCE;
        typeClassName = clazz.getCanonicalName();
    }
}
