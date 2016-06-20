package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Utf8Item extends ConstantPoolItem {

    private String value;

    public Utf8Item(String value) {
        super(1);
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utf8Item utf8Item = (Utf8Item) o;

        return value != null ? value.equals(utf8Item.value) : utf8Item.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, value.getBytes().length);
        WriterUtil.write(os, value.getBytes());
    }
}
