package org.github.holmistr.javacompiler.element;

import org.github.holmistr.javacompiler.element.statement.Statement;
import org.github.holmistr.javacompiler.instruction.Instruction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class method.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Method {

    private String name;
    private String returnType;
    private List<AccessFlag> accessFlags = Collections.emptyList();
    private List<Parameter> parameters = Collections.emptyList();
    private List<Statement> body = Collections.emptyList();

    private List<Instruction> instructions = new ArrayList<>();
    private int maxStack = 0;
    private int maxLocals = 0;

    public boolean isConstructor() {
        return returnType == null;
    }

    public List<AccessFlag> getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(List<AccessFlag> accessFlags) {
        this.accessFlags = accessFlags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Statement> getBody() {
        return body;
    }

    public void setBody(List<Statement> body) {
        this.body = body;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        if (returnType != null ? !returnType.equals(method.returnType) : method.returnType != null) return false;
        if (accessFlags != null ? !accessFlags.equals(method.accessFlags) : method.accessFlags != null) return false;
        if (parameters != null ? !parameters.equals(method.parameters) : method.parameters != null) return false;
        if (body != null ? !body.equals(method.body) : method.body != null) return false;
        return instructions != null ? instructions.equals(method.instructions) : method.instructions == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        result = 31 * result + (accessFlags != null ? accessFlags.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Method{" +
                "accessFlags=" + accessFlags +
                ", name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                ", instructions=" + instructions +
                '}';
    }
}
