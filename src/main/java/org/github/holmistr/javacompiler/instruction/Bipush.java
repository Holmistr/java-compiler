package org.github.holmistr.javacompiler.instruction;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Bipush extends Instruction {

    public static final int LENGTH = 2;

    private int value;

    public Bipush(int value) {
        super(0x10);
        this.value = value;
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, value, 1);
    }
}
