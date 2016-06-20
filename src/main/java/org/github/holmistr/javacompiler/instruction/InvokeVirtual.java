package org.github.holmistr.javacompiler.instruction;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class InvokeVirtual extends Instruction {

    public static final int LENGTH = 3;

    private int operand;

    public InvokeVirtual(int operand) {
        super(0xB6);
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
        WriterUtil.write(os, getOperand());
    }
}
