package org.github.holmistr.javacompiler.instruction;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Isub extends Instruction {

    public static final int LENGTH = 1;

    public Isub() {
        super(0x64);
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
