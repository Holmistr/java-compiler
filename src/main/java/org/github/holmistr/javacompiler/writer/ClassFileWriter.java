package org.github.holmistr.javacompiler.writer;

import org.github.holmistr.javacompiler.element.File;
import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class encapsulates all writes into the class file being created.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ClassFileWriter {

    private static final int MAGIC = 0xCAFEBABE;
    private static final byte MINOR_VERSION = 0;
    private static final byte MAJOR_VERSION = 52;

    private String outputFilename;

    public ClassFileWriter(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public void write(File file) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new java.io.File(outputFilename)))){
            WriterUtil.write(dos, MAGIC, 4);
            WriterUtil.write(dos, MINOR_VERSION);
            WriterUtil.write(dos, MAJOR_VERSION);

            new FileWriter(file, dos).write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
