package org.github.holmistr.javacompiler.instruction;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Iload extends Instruction {

    public static final int LENGTH = 2;

    private int operand;

    public Iload(int operand) {
        super(0x15);
        this.operand = operand;
    }

    public int getOperand() {
        return operand;
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, getOperand(), 1);
    }
}
