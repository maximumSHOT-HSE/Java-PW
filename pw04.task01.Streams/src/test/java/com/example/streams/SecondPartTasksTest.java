package com.example.streams;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.streams.SecondPartTasks.*;
import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {

    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(abs(piDividedBy4() - Math.PI / 4) <= 0.01);
    }

    @Test
    public void testFindPrinter() {
        var testMap = new TreeMap<String, List<String>>();

        testMap.put("aaa", List.of("a", "bbbb", "c"));
        testMap.put("bbb", List.of("a", "bbb", "c"));
        testMap.put("ccc", List.of("a", "b", "c", "d"));
        testMap.put("ddd", List.of("abbbc"));

        assertEquals("aaa", findPrinter(testMap));
    }

    @Test
    public void testCalculateGlobalOrder() {
        var m1 = new TreeMap<String, Integer>();
        m1.put("a", 1);
        m1.put("b", 2);

        var m2 = new TreeMap<String, Integer>();
        m2.put("a", 1);

        var list = List.of((Map<String, Integer>) m1, (Map<String, Integer>) m2);

        var m3 = calculateGlobalOrder(list);
        assertEquals(2, (int) m3.get("a"));
        assertEquals(2, (int) m3.get("b"));
    }
}