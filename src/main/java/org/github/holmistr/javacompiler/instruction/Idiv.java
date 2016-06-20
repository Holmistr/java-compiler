package org.github.holmistr.javacompiler.instruction;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Idiv extends Instruction {

    public static final int LENGTH = 1;

    public Idiv() {
        super(0x6c);
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
