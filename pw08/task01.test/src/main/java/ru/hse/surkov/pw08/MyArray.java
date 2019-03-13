package ru.hse.surkov.pw08;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class MyArray <V> {

    @NotNull private Object[] array;

    public MyArray(int startCapacity) {
        array = new Object[startCapacity];
        clear();
    }

    public int size() {
        return array.length;
    }

    @Nullable public V get(int position) {
        return (V) array[position];
    }

    public void put(int position, @Nullable V value) {
        array[position] = value;
    }

    public void remove(int position) {
        array[position] = null;
    }

    public void clear() {
        Arrays.fill(array, null);
    }
}
