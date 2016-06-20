package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * Abstract class for types of items in constant pool.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public abstract class ConstantPoolItem {

    protected int code;

    public ConstantPoolItem(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void write(OutputStream os) {
        WriterUtil.write(os, getCode(), 1);
    }

}
