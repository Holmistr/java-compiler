package org.github.holmistr.javacompiler.processor;

import org.github.holmistr.javacompiler.constantpool.Constants;
import org.github.holmistr.javacompiler.element.AccessFlag;
import org.github.holmistr.javacompiler.element.Class;
import org.github.holmistr.javacompiler.element.ConstantPool;
import org.github.holmistr.javacompiler.element.Method;
import org.github.holmistr.javacompiler.instruction.Aload;
import org.github.holmistr.javacompiler.instruction.InvokeSpecial;
import org.github.holmistr.javacompiler.util.ByteCodeUtil;

import java.util.Arrays;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class MethodsProcessor {

    private ConstantPool constantPool;
    private Class clazz;

    public MethodsProcessor(ConstantPool constantPool, Class clazz) {
        this.constantPool = constantPool;
        this.clazz = clazz;
    }

    public void processMethods() {
        constantPool.addUtf8(Constants.CODE_ATTRIBUTE); //mandatory attribute for method

        if (!clazz.hasConstructor()) {
            createDefaultConstructor();
        }

        clazz.getMethods().stream().forEach(method -> processMethod(method));
    }

    private void processMethod(Method method) {
        constantPool.addUtf8(method.getName());
        constantPool.addUtf8(ByteCodeUtil.createMethodDescriptor(method));

        new MethodBodyProcessor(constantPool, method).processBody();
    }

    private void createDefaultConstructor() {
        Method constructor = new Method();
        constructor.setName(Constants.DEFAULT_CONSTRUCTOR);
        constructor.setAccessFlags(Arrays.asList(AccessFlag.PUBLIC));

        Aload instruction1 = new Aload(0);
        constructor.getInstructions().add(instruction1);

        int superClassNameIndex = constantPool.addUtf8(clazz.getSuperClass());
        int superClassIndex = constantPool.addClass(superClassNameIndex);
        int constructorNameIndex = constantPool.addUtf8(Constants.DEFAULT_CONSTRUCTOR);
        int constructorDescriptorIndex = constantPool.addUtf8("()" + Constants.TYPE_VOID);
        int nameAndTypeIndex = constantPool.addNameAndType(constructorNameIndex, constructorDescriptorIndex);
        int methodRefIndex = constantPool.addMethodRef(superClassIndex, nameAndTypeIndex);

        InvokeSpecial instruction2 = new InvokeSpecial(methodRefIndex);
        constructor.getInstructions().add(instruction2);

        constructor.setMaxStack(1);
        constructor.setMaxLocals(1);

        clazz.getMethods().add(constructor);
    }


}
