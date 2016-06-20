package org.github.holmistr.javacompiler.writer;

import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.util.ByteCodeUtil;
import org.github.holmistr.javacompiler.util.WriterUtil;

import java.io.OutputStream;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MethodsWriter {

    private OutputStream dos;
    private Class clazz;
    private ConstantPool constantPool;

    public MethodsWriter(OutputStream dos, Class clazz, ConstantPool constantPool) {
        this.clazz = clazz;
        this.constantPool = constantPool;
        this.dos = dos;
    }

    public void write() {
        WriterUtil.write(dos, clazz.getMethods().size());
        clazz.getMethods().stream().forEach(method -> write(method));
    }

    private void write(Method method) {
        int accessFlagsCode = 0;
        for (AccessFlag flag: method.getAccessFlags()) {
            accessFlagsCode += flag.getValue();
        }
        WriterUtil.write(dos, accessFlagsCode);
        WriterUtil.write(dos, constantPool.getUtf8(method.getName()));
        WriterUtil.write(dos, constantPool.getUtf8(ByteCodeUtil.createMethodDescriptor(method)));

        WriterUtil.write(dos, 1); //attributes_count
        //writing only "Code" attribute
        int codeStringIndex = constantPool.getUtf8("Code");
        WriterUtil.write(dos, codeStringIndex);


        int codeLength = method.getInstructions().stream().mapToInt(instruction -> instruction.getLength()).sum();
        int codeAttributeLength = 2 //max_stack
                + 2 //max_locals
                + 4 //code_length
                + codeLength //TODO: code
                + 2 //exception table length
                + 2; //attributes_count

        WriterUtil.write(dos, codeAttributeLength, 4);
        WriterUtil.write(dos, method.getMaxStack());
        WriterUtil.write(dos, method.getMaxLocals());
        WriterUtil.write(dos, codeLength, 4);

        method.getInstructions().stream().forEach(instruction -> instruction.write(dos));

        WriterUtil.write(dos, 0); //exception_table_length - we do not support exceptions
        WriterUtil.write(dos, 0); //attributes_count - no additional attributes attached
    }
}
