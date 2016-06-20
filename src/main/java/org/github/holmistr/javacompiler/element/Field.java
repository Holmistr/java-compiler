package org.github.holmistr.javacompiler.element;

import java.util.List;

/**
 * Class field.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Field {

    private String name;
    private String type;
    private List<AccessFlag> accessFlags;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        if (type != null ? !type.equals(field.type) : field.type != null) return false;
        return accessFlags != null ? accessFlags.equals(field.accessFlags) : field.accessFlags == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (accessFlags != null ? accessFlags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "accessFlags=" + accessFlags +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
