package org.github.holmistr.javacompiler.instruction;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class IfIcmpGt extends Instruction {

    public static final int LENGTH = 3;

    private int branch;

    public IfIcmpGt(int branch) {
        super(0xa2);
        this.branch = branch;
    }

    public int getBranch() {
        return branch;
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, getBranch(), 2);
    }
}
