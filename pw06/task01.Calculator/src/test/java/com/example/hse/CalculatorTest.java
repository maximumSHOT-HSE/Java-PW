package com.example.hse;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculatorTest {

    @Test
    void testPlus() {
        var calculator = new Calculator(new ArrayList<>());
        assertEquals(3, (int) calculator.calculate("1 2 +"));

    }

    @Test
    void testMockPlus() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(3, 4, 7);

        assertEquals(7, (int) calculator.calculate("3 4 +"));
    }
}