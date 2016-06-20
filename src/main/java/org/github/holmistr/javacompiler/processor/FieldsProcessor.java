package org.github.holmistr.javacompiler.processor;

import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.ConstantPool;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class FieldsProcessor {

    private ConstantPool constantPool;
    private Class clazz;

    public FieldsProcessor(ConstantPool constantPool, Class clazz) {
        this.constantPool = constantPool;
        this.clazz = clazz;
    }

    public void processFields() {

    }
}
