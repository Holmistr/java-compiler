package org.github.holmistr.javacompiler.util;

import org.github.holmistr.javacompiler.constantpool.Constants;
import org.github.holmistr.javacompiler.element.expression.Expression;
import org.github.holmistr.javacompiler.element.expression.IdentifierExpression;
import org.github.holmistr.javacompiler.element.expression.NewExpression;
import org.github.holmistr.javacompiler.processor.ExpressionType;
import org.github.holmistr.javacompiler.processor.Variable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class TypeUtil {

    public enum Type {
        INT(Constants.TYPE_INT, true), BOOLEAN(Constants.TYPE_BOOLEAN, true), REFERENCE(Constants.TYPE_REFERENCE, false);

        private String byteCodeValue;
        private boolean isPrimitive;

        Type(String value, boolean isPrimitive) {
            this.byteCodeValue = value;
            this.isPrimitive = isPrimitive;
        }

        public String getByteCodeValue() {
            return byteCodeValue;
        }

        public boolean isPrimitive() {
            return isPrimitive;
        }
    }

    public static Type parseType(String string) {
        if (isArray(string)) {
            string = getCoreTypeOfArray(string);
        }
        switch (string) {
            case "int":
                return Type.INT;

            case "boolean":
                return Type.BOOLEAN;

            default:
                return Type.REFERENCE;
        }
    }

    public static String createFileFormat(String string) {
        return string.replace(".", "/");
    }

    public static String getFullNameInFileFormat(String type) {
        return createFileFormat(getFullNameInDotFormat(type));
    }

    public static String getFullNameInDotFormat(String type) {
        if (isArray(type)) {
            type = getCoreTypeOfArray(type);
        }
        List<String> imports = Arrays.asList("java.lang", "java.util", "");
        for (String importVar: imports) {
            try {
                String fullName = !importVar.equals("") ? importVar + "." + type : type;
                Class.forName(fullName);
                return fullName;
            } catch (ClassNotFoundException ex) {}
        }

        throw new RuntimeException("Class '" + type + "' not found in the specified imports.");
    }

    public static boolean isArray(String type) {
        return type.contains("[");
    }

    public static String getCoreTypeOfArray(String type) {
        return type.replace("[", "").replace("]", "");
    }

    public static Class[] getMethodParameterClasses(Map<String, Variable> variables, List<Expression> arguments) {
        Class[] types = new Class[arguments.size()];
        for (int i = 0; i < types.length; i++) {
            Expression expression = arguments.get(i);
            expression.resolveType(variables);
            if (expression.getType() == ExpressionType.STRING) {
                types[i] = String.class;
            } else if (expression.getType() == ExpressionType.INT) {
                types[i] = int.class;
            } else if (expression.getType() == ExpressionType.BOOLEAN) {
                types[i] = boolean.class;
            } else if (expression.getType() == ExpressionType.REFERENCE) {
                if (expression instanceof NewExpression) {
                    Class clazz = null;
                    try {
                        clazz = Class.forName(TypeUtil.getFullNameInDotFormat(((NewExpression) expression).getClassName()))                    ;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    types[i] = clazz;
                } else if (expression instanceof IdentifierExpression) {
                    String className = variables.get(((IdentifierExpression) expression).getValue()).getClassName();
                    Class clazz = null;
                    try {
                        clazz = Class.forName(TypeUtil.getFullNameInDotFormat(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    types[i] = clazz;
                }
            }
        }

        return types;
    }
}
