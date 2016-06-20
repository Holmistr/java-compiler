package org.github.holmistr.javacompiler.element;

import org.github.holmistr.javacompiler.constantpool.*;

import java.util.*;

/**
 * Class representing constant pool.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class ConstantPool {

    private int poolIndex = 1;

    private Map<Integer, ConstantPoolItem> pool = new TreeMap<>();
    private Map<ConstantPoolItem, Integer> searchIndex = new HashMap<>();

    /**
     * Adds new item into constant pool and retrieves the index assigned to it. If the item is already present
     * in the constant pool, it doesn't do anything.
     *
     * @param item
     * @return index of the item in the constant pool
     */
    private int add(ConstantPoolItem item) {
        if (searchIndex.containsKey(item)) {
            return searchIndex.get(item);
        }

        pool.put(poolIndex, item);
        searchIndex.put(item, poolIndex);
        int poolIndexToReturn = poolIndex;
        poolIndex++;
        return poolIndexToReturn;
    }

    private int get(ConstantPoolItem item) {
        if (searchIndex.containsKey(item)) {
            return searchIndex.get(item);
        }

        throw new IllegalArgumentException("Item not found in constant pool.");
    }

    public int getSize() {
        return pool.size();
    }

    public List<ConstantPoolItem> getItems() {
        return new ArrayList<>(pool.values());
    }

    public int addUtf8(String string) {
        Utf8Item item = new Utf8Item(string);
        return add(item);
    }

    public int addString(int index) {
        StringItem item = new StringItem(index);
        return add(item);
    }

    public int addClass(int index) {
        ClassItem item = new ClassItem(index);
        return add(item);
    }

    public int addNameAndType(int name, int descriptor) {
        NameAndTypeItem item = new NameAndTypeItem(name, descriptor);
        return add(item);
    }

    public int addMethodRef(int classIndex, int nameAndTypeIndex) {
        MethodRefItem item = new MethodRefItem(classIndex, nameAndTypeIndex);
        return add(item);
    }

    public int addFieldRef(int classIndex, int nameAndTypeIndex) {
        FieldRefItem item = new FieldRefItem(classIndex, nameAndTypeIndex);
        return add(item);
    }

    public int getClass(String className) {
        ClassItem classItem = new ClassItem(getUtf8(className));
        return get(classItem);
    }

    public int getUtf8(String string) {
        Utf8Item classNameItem = new Utf8Item(string);
        return get(classNameItem);
    }
}
