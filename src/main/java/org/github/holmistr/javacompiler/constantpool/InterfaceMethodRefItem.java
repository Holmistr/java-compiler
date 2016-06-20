package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * Represents CONSTANT_InterfaceMethodref_in.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class InterfaceMethodRefItem extends ConstantPoolItem {

    private int classIndex;
    private int nameAndTypeIndex;

    public InterfaceMethodRefItem(int classIndex, int nameAndTypeIndex) {
        super(11);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceMethodRefItem that = (InterfaceMethodRefItem) o;

        if (classIndex != that.classIndex) return false;
        return nameAndTypeIndex == that.nameAndTypeIndex;
    }

    @Override
    public int hashCode() {
        int result = classIndex;
        result = 31 * result + nameAndTypeIndex;
        return result;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, classIndex);
        WriterUtil.write(os, nameAndTypeIndex);
    }
}
