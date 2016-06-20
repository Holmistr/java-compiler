package org.github.holmistr.javacompiler.processor;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class Variable {

    private String identifier;
    private Integer framePosition;
    private ExpressionType type;
    private String className;

    public Integer getFramePosition() {
        return framePosition;
    }

    public void setFramePosition(Integer framePosition) {
        this.framePosition = framePosition;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ExpressionType getType() {
        return type;
    }

    public void setType(ExpressionType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
