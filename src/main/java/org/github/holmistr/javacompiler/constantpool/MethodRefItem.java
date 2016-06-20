package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * Represents  CONSTANT_Methodref_info.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MethodRefItem extends ConstantPoolItem {

    private int classIndex;
    private int nameAndTypeIndex;

    public MethodRefItem(int classIndex, int nameAndTypeIndex) {
        super(10);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodRefItem that = (MethodRefItem) o;

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
