package ru.hse.surkov.pw08;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {

    private MyList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void testAdd() {
        list.add("abc");
        list.add("def");
        list.add(null);
        list.add("qwe");

        ArrayList<String> helper = new ArrayList<>();

        helper.add("abc");
        helper.add("def");
        helper.add(null);
        helper.add("qwe");

        Iterator<String> current = list.iterator();

        for (var expected : helper) {
            assertTrue(current.hasNext());
            assertEquals(expected, current.next());
        }

        assertFalse(current.hasNext());
    }

    @Test
    void testClear() {
        list.add("abc");
        list.add("def");
        list.add(null);
        list.add("qwe");

        list.clear();

        assertFalse(list.iterator().hasNext());

        list.add("abc");
        list.add("def");

        ArrayList<String> helper = new ArrayList<>();

        helper.add("abc");
        helper.add("def");

        Iterator<String> current = list.iterator();

        for (var expected : helper) {
            assertTrue(current.hasNext());
            assertEquals(expected, current.next());
        }

        assertFalse(current.hasNext());
    }
}