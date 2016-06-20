package org.github.holmistr.javacompiler.element;

import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * Access flags (for fields, methods etc.)
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public enum AccessFlag {

    PUBLIC(0x0001),
    PRIVATE(0x0002),
    SUPER(0x0020),
    STATIC(0x0008);

    private int value;

    AccessFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AccessFlag parse(String string) {
        for (AccessFlag accessFlag: values()) {
            if (accessFlag.toString().equalsIgnoreCase(string)) {
                return accessFlag;
            }
        }

        return null;
    }
}
