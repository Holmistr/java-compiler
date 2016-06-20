package org.github.holmistr.javacompiler.processor;

import org.github.holmistr.javacompiler.constantpool.Constants;
import org.github.holmistr.javacompiler.constantpool.Utf8Item;
import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.element.File;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class BaseProcessor {

    private ConstantPool constantPool;
    private File file;

    public BaseProcessor(ConstantPool constantPool, File file) {
        this.constantPool = constantPool;
        this.file = file;
    }

    public void processFile() {
        addFileInfo();

        ClassProcessor classProcessor = new ClassProcessor(constantPool, file.getClazz());
        classProcessor.processClass();
    }

    private void addFileInfo() {
        constantPool.addUtf8(Constants.SOURCE_FILE);
        constantPool.addUtf8(file.getFilename());
    }
}
