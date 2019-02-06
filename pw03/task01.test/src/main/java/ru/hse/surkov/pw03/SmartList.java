package ru.hse.surkov.pw03;

import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SmartList<E> extends AbstractList<E> implements List<E> {

    private int size;
    @Nullable Object data;

    public SmartList() {
        size = 0;
        data = null;
    }

    public SmartList(@Nullable Collection<E> collection) {
        size = 0;
        data = null;
        if (collection != null) {
            for (var element : collection) {
                add(element);
            }
        }
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        size--;
        Object buffer = null;
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

    @Override
    public int size() {
        return size;
    }
}
