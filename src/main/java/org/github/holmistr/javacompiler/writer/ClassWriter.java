package org.github.holmistr.javacompiler.writer;

import java.io.DataOutputStream;
import java.io.OutputStream;

import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.util.WriterUtil;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ClassWriter {

    private OutputStream dos;
    private Class clazz;
    private ConstantPool constantPool;

    public ClassWriter(OutputStream dos, Class clazz, ConstantPool constantPool) {
        this.dos = dos;
        this.clazz = clazz;
        this.constantPool = constantPool;
    }

    public void write() {
        int accessFlagsCode = 0;
        for (AccessFlag flag: clazz.getAccessFlags()) {
            accessFlagsCode += flag.getValue();
        }
        WriterUtil.write(dos, accessFlagsCode);
        WriterUtil.write(dos, constantPool.getClass(clazz.getThisClass()));
        WriterUtil.write(dos, constantPool.getClass(clazz.getSuperClass()));
        WriterUtil.write(dos, 0); //interface count, we do not support interfaces
        WriterUtil.write(dos, clazz.getFields().size());
        //TODO: add writing of fields
        new MethodsWriter(dos, clazz, constantPool).write();
        //TODO: add source file
    }
}
