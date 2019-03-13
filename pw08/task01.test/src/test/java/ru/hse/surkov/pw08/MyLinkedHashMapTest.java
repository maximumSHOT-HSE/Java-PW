package ru.hse.surkov.pw08;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedHashMapTest {

    private MyLinkedHashMap<String, Integer> hashMap;

    @BeforeEach
    void setUp() {
        hashMap = new MyLinkedHashMap<>();
    }

    @Test
    void testAddManyElements() {
        for (int i = 0; i < 100; i++) {
            hashMap.put(Integer.toString(i), i);
        }
    }

    @Test
    void testAddDuplicates() {
        String s = "a";
        for (int i = 0;i < 10; i++) {
            hashMap.put(s, i);
            assertEquals(java.util.Optional.of(i), java.util.Optional.ofNullable(hashMap.get(s)));
        }
    }

    @Test
    void testAddManyElementswithDuplicats() {
        for (int iteration = 0; iteration < 10; iteration++) {
            for (int key = 0; key < 10; key++) {
                for (int value = 0; value < 10; value++) {
                    hashMap.put(Integer.toString(key), value);
                    assertEquals(java.util.Optional.of(value), java.util.Optional.ofNullable(hashMap.get(Integer.toString(key))));
                }
            }
        }
    }

    @Test
    void testBasicRemove() {
        for (int i = 0;i < 100; i++) {
            hashMap.put(Integer.toString(i), i);
        }
        for (int i = 0; i < 50; i += 2) {
            assertEquals(java.util.Optional.of(i), java.util.Optional.ofNullable(hashMap.remove(Integer.toString(i))));
            hashMap.remove(Integer.toString(i));
        }
        for (int i = 0;i < 50; i++) {
            if (i % 2 == 1) {
                assertEquals(java.util.Optional.of(i), java.util.Optional.ofNullable(hashMap.get(Integer.toString(i))));
            }
        }
    }
}