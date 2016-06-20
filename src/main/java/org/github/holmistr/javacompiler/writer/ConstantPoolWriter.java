package org.github.holmistr.javacompiler.writer;

import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ConstantPoolWriter {

    private OutputStream dos;
    private ConstantPool constantPool;

    public ConstantPoolWriter(OutputStream dos, ConstantPool constantPool) {
        this.dos = dos;
        this.constantPool = constantPool;
    }

    public void write() {
        WriterUtil.write(dos, constantPool.getSize() + 1); //+1 because of the specification
        constantPool.getItems().stream().forEach(item -> item.write(dos));
    }
}
