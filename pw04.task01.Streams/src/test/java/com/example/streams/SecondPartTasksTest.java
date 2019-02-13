package com.example.streams;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.example.streams.SecondPartTasks.*;
import static org.junit.jupiter.api.Assertions.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {

    }

    @Test
    public void testPiDividedBy4() {
        assertTrue((piDividedBy4() - Math.PI / 4) <= 0.0001);
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
        fail();
    }
}