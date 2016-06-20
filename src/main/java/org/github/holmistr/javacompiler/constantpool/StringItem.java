package org.github.holmistr.javacompiler.constantpool;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class StringItem extends ConstantPoolItem {

    private int valueIndex;

    public StringItem(int valueIndex) {
        super(8);
        this.valueIndex = valueIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringItem classItem = (StringItem) o;

        return valueIndex == classItem.valueIndex;
    }

    @Override
    public int hashCode() {
        return valueIndex;
    }

    @Override
    public void write(OutputStream os) {
        super.write(os);
        WriterUtil.write(os, valueIndex);
    }
}
