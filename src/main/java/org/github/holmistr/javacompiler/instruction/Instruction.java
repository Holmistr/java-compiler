package org.github.holmistr.javacompiler.instruction;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public abstract class Instruction {

    private int code;

    public Instruction(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void write(OutputStream os) {
        WriterUtil.write(os, getCode(), 1);
    }

    public abstract int getLength();
}
