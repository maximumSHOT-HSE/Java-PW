package ru.hse.surkov.pw03;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Mutable list with optimized approach of storing
 * a small amount of elements.
 * */
public class SmartList<E> extends AbstractList<E> implements List<E> {

    private int size;
    @Nullable private Object data;

    /** Constructs an empty list */
    public SmartList() {
        size = 0;g
        data = null;
    }

    /**
     * Constructs a new list using received collection.
     * Elements will be added in the same order.
     * */
    public SmartList(@Nullable Collection<E> collection) {
        size = 0;
        data = null;
        if (collection != null) {
            this.addAll(collection);
        }
    }

    /** {@link List#remove(int)} */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        size--;
        Object buffer;
        if (size + 1 == 1) {
            buffer = data;
            data = null;
        } else if (size + 1 <= 5) {
            var objArray = (Object[]) data;
            buffer = objArray[index];
            objArray[index] = null;
            for (int i = index; i + 1 < size + 1; i++) {
                objArray[i] = objArray[i + 1];
            }
            if (size + 1 == 2) {
                data = objArray[0];
            } else {
                data = objArray;
            }
        } else {
            var objList = (ArrayList) data;
            buffer = objList.remove(index);
            if (size + 1 == 6) {
                data = objList.toArray();
            } else {
                data = objList;
            }
        }
        return (E) buffer;
    }

    /** {@link List#add(Object)} */
    @Override
    public boolean add(@Nullable E e) {
        size++;
        if (size == 1) {
            data = e;
        } else if (size == 2) {
            Object buffer = data;
            Object[] objArray = new Object[5];
            objArray[0] = buffer;
            objArray[1] = e;
            data = objArray;
        } else if (size <= 5) {
            var objArray = (Object[]) data;
            objArray[size - 1] = e;
            data = objArray;
        } else if (size == 6) {
            var objList = new ArrayList();
            var objArray = (Object[]) data;
            for (int i = 0; i < 5; i++) {
                objList.add(objArray[i]);
            }
            objList.add(e);
            data = objList;
        } else {
            var objList = (ArrayList) data;
            objList.add(e);
        }
        return true;
    }

    /** {@link List#get(int)} */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            return (E) data;
        } else if (size <= 5) {
            var objArray = (Object[]) data;
            return (E) objArray[index];
        } else {
            var objList = (ArrayList) data;
            return (E) objList.get(index);
        }
    }

    /** {@link List#set(int, Object)} */
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Object buffer;
        if (size == 1) {
            buffer = data;
            data = element;
        } else if (size <= 5) {
            var objArray = (Object[]) data;
            buffer = objArray[index];
            objArray[index] = element;
        } else {
            var objList = (ArrayList) data;
            buffer = objList.set(index, element);
        }
        return (E) buffer;
    }

    /** {@link List#size()} */
    @Override
    public int size() {
        return size;
    }
}
