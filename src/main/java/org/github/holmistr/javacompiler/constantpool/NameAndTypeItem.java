package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class NameAndTypeItem extends ConstantPoolItem {

    private int name;
    private int descriptor;

    public NameAndTypeItem(int name, int descriptor) {
        super(12);
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameAndTypeItem that = (NameAndTypeItem) o;

        if (name != that.name) return false;
        return descriptor == that.descriptor;
    }

    @Override
    public int hashCode() {
        int result = name;
        result = 31 * result + descriptor;
        return result;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, name);
        WriterUtil.write(os, descriptor);
    }
}
