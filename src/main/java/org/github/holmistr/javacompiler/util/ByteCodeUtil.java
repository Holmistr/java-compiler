package org.github.holmistr.javacompiler.util;

import org.github.holmistr.javacompiler.constantpool.Constants;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.element.Parameter;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ByteCodeUtil {

    public static String createMethodDescriptor(Method method) {
        StringBuilder descriptor = new StringBuilder();
        descriptor.append("(");
        for (Parameter parameter: method.getParameters()) {
            descriptor.append(createTypeDescriptor(parameter.getType()));
        }

        descriptor.append(")");
        if (method.getReturnType() != null && !method.getReturnType().equals("void")) {
            descriptor.append(TypeUtil.parseType(method.getReturnType()).getByteCodeValue());
        } else {
            descriptor.append(Constants.TYPE_VOID);
        }

        return descriptor.toString();
    }

    public static String createMethodDescriptor(java.lang.reflect.Method method) {
        StringBuilder descriptor = new StringBuilder();
        descriptor.append("(");

        for (Class type: method.getParameterTypes()) {
            descriptor.append(createTypeDescriptor(type.getName()));
        }

        descriptor.append(")");

        if (method.getReturnType() != null && !method.getReturnType().getName().equals("void")) {
            descriptor.append(TypeUtil.parseType(method.getReturnType().getName()).getByteCodeValue());
        } else {
            descriptor.append(Constants.TYPE_VOID);
        }

        return descriptor.toString();
    }

    public static String createMethodDescriptor(java.lang.reflect.Constructor constructor) {
        StringBuilder descriptor = new StringBuilder();
        descriptor.append("(");

        for (Class type: constructor.getParameterTypes()) {
            descriptor.append(createTypeDescriptor(type.getName()));
        }
        descriptor.append(")");
        descriptor.append(Constants.TYPE_VOID);

        return descriptor.toString();
    }

    public static String createTypeDescriptor(String string) {
        StringBuilder descriptor = new StringBuilder();
        if (TypeUtil.isArray(string)) {
            descriptor.append("[");
        }
        TypeUtil.Type type = TypeUtil.parseType(string);
        descriptor.append(type.getByteCodeValue());
        if (!type.isPrimitive()) {
            descriptor.append(TypeUtil.getFullNameInFileFormat(string));
            descriptor.append(";");
        }

        return descriptor.toString();
    }

}
