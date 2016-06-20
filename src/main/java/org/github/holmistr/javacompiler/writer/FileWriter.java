package org.github.holmistr.javacompiler.writer;

import org.github.holmistr.javacompiler.constantpool.Constants;
import org.github.holmistr.javacompiler.element.File;
import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class FileWriter {

    private File file;
    private OutputStream dos;

    public FileWriter(File file, OutputStream dos) {
        this.file = file;
        this.dos = dos;
    }

    public void write() {
        new ConstantPoolWriter(dos, file.getConstantPool()).write();
        new ClassWriter(dos, file.getClazz(), file.getConstantPool()).write();
        writeSourceFile();
    }

    private void writeSourceFile() {
        WriterUtil.write(dos, 1); //one additional attribute SourceFile
        WriterUtil.write(dos, file.getConstantPool().getUtf8(Constants.SOURCE_FILE));
        WriterUtil.write(dos, 2, 4); //attribute_length for SourceFile is 2
        WriterUtil.write(dos, file.getConstantPool().getUtf8(file.getFilename()));
    }

}
