package org.github.holmistr.javacompiler.instruction;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Imul extends Instruction {

    public static final int LENGTH = 1;

    public Imul() {
        super(0x68);
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
    }
}
