package org.github.holmistr.javacompiler.instruction;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Aload extends Instruction {

    public static final int LENGTH = 2;

    private int operand;

    public Aload(int operand) {
        super(0x19);
        this.operand = operand;
    }

    public int getOperand() {
        return operand;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, getOperand(), 1);
    }

    @Override
    public int getLength() {
        return LENGTH;
    }
}
