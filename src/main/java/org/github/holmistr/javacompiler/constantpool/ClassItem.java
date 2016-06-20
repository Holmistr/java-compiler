package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ClassItem extends ConstantPoolItem {

    private int nameIndex;

    public ClassItem(int nameIndex) {
        super(7);
        this.nameIndex = nameIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassItem classItem = (ClassItem) o;

        return nameIndex == classItem.nameIndex;
    }

    @Override
    public int hashCode() {
        return nameIndex;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, nameIndex);
    }
}
