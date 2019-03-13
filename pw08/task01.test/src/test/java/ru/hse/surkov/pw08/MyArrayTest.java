package ru.hse.surkov.pw08;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyArrayTest {

    private MyArray<String> array;

    @BeforeEach
    void setUp() {
        array = new MyArray<>(10);
    }

    @Test
    void testStartSize() {
        for (int size = 0; size < 10; size++) {
            array = new MyArray<>(size);
            assertEquals(size, array.size());
        }
    }

    @Test
    void testGet() {
        array.put(0, "abc");
        assertEquals("abc", array.get(0));
        array.put(0, "def");
        assertEquals("def", array.get(0));;
        for (int i = 0; i < array.size(); i++) {
            array.put(i, Integer.toString(i));
        }
        for (int i = 0; i < array.size(); i++) {
            assertEquals(Integer.toString(i), array.get(i));
        }
    }
}