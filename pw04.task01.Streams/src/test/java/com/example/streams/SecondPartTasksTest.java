package com.example.streams;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static com.example.streams.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        fail();
    }

    @Test
    public void testPiDividedBy4() {
        System.out.println(piDividedBy4());
    }

    @Test
    public void testFindPrinter() {
        var testMap = new TreeMap<String, List<String>>();

        

        assertEquals("aaa", findPrinter(testMap));
    }

    @Test
    public void testCalculateGlobalOrder() {
        fail();
    }
}