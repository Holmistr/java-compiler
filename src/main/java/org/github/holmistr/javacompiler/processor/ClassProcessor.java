package org.github.holmistr.javacompiler.processor;

import org.github.holmistr.javacompiler.constantpool.*;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.ConstantPool;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ClassProcessor {

    private ConstantPool constantPool;
    private Class clazz;

    public ClassProcessor(ConstantPool constantPool, Class clazz) {
        this.constantPool = constantPool;
        this.clazz = clazz;
    }

    public void processClass() {
        addBasicInfo();

        new FieldsProcessor(constantPool, clazz).processFields();
        new MethodsProcessor(constantPool, clazz).processMethods();
    }

    private void addBasicInfo() {
        int classNameIndex = constantPool.addUtf8(clazz.getThisClass());
        constantPool.addClass(classNameIndex);

        int superClassStringIndex = constantPool.addUtf8(clazz.getSuperClass());
        constantPool.addClass(superClassStringIndex);
    }
}
