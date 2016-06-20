package org.github.holmistr.javacompiler.element;

import org.github.holmistr.javacompiler.constantpool.Constants;

import java.util.Collections;
import java.util.List;

/**
 * Class representing whole class (class file).
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Class {

    private List<AccessFlag> accessFlags = Collections.emptyList();
    private String thisClass;
    private String superClass = Constants.OBJECT_CLASS;

    private List<Field> fields = Collections.emptyList();
    private List<Method> methods = Collections.emptyList();

    public boolean hasConstructor() {
        return methods.stream().anyMatch(method -> method.isConstructor());
    }

    public List<AccessFlag> getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(List<AccessFlag> accessFlags) {
        this.accessFlags = accessFlags;
    }

    public String getThisClass() {
        return thisClass;
    }

    public void setThisClass(String thisClass) {
        this.thisClass = thisClass;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Class aClass = (Class) o;

        if (accessFlags != null ? !accessFlags.equals(aClass.accessFlags) : aClass.accessFlags != null) return false;
        if (thisClass != null ? !thisClass.equals(aClass.thisClass) : aClass.thisClass != null) return false;
        if (superClass != null ? !superClass.equals(aClass.superClass) : aClass.superClass != null) return false;
        if (fields != null ? !fields.equals(aClass.fields) : aClass.fields != null) return false;
        return methods != null ? methods.equals(aClass.methods) : aClass.methods == null;
    }

    @Override
    public int hashCode() {
        int result = accessFlags != null ? accessFlags.hashCode() : 0;
        result = 31 * result + (thisClass != null ? thisClass.hashCode() : 0);
        result = 31 * result + (superClass != null ? superClass.hashCode() : 0);
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Class{" +
                "accessFlags=" + accessFlags +
                ", thisClass='" + thisClass + '\'' +
                ", superClass='" + superClass + '\'' +
                ", fields=" + fields +
                ", methods=" + methods +
                '}';
    }
}
