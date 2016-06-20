package org.github.holmistr.javacompiler.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class WriterUtil {

    private static final int DEFAULT_NUMBER_OF_BYTES = 2;

    public static void write(OutputStream dos, int value) {
        write(dos, value, DEFAULT_NUMBER_OF_BYTES);
    }

    public static void write(OutputStream dos, int value, int numberOfBytes) {
        int sizeOfBuffer = 4;
        ByteBuffer bb = ByteBuffer.allocate(sizeOfBuffer);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(value);
        byte[] array = bb.array();

        for (int i = sizeOfBuffer-numberOfBytes; i < sizeOfBuffer; i++) {
            try {
                dos.write(array[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void write(OutputStream dos, byte[] value) {
        try {
            dos.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
