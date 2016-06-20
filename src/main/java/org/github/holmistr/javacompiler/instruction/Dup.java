package org.github.holmistr.javacompiler.instruction;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Dup extends Instruction {

    public static final int LENGTH = 1;

    public Dup() {
        super(0x59);
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
