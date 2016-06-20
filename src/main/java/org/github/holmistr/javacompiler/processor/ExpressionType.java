package org.github.holmistr.javacompiler.processor;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public enum ExpressionType {

    STRING, INT, BOOLEAN, REFERENCE;

    public static ExpressionType parse(String string) {
        if ("int".equalsIgnoreCase(string)) {
            return INT;
        } else if ("boolean".equalsIgnoreCase(string)) {
            return BOOLEAN;
        } else {
            return REFERENCE;
        }
    }
}
