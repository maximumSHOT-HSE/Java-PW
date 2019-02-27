package com.example.hse;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testPlus() {
        var calculator = new Calculator(new ArrayList<>());
        assertEquals(3, (int) calculator.calculate("1 2 +"));
    }

    @Test
    void testMinus() {
        var calculator = new Calculator(new ArrayList<>());
        assertEquals(-1, (int) calculator.calculate("1 2 -"));
    }

    @Test
    void testDivide() {
        var calculator = new Calculator(new ArrayList<>());
        assertEquals(4, (int) calculator.calculate("20 5 /"));
    }

    @Test
    void testMultyply() {
        var calculator = new Calculator(new ArrayList<>());
        assertEquals(12, (int) calculator.calculate("3 4 *"));
    }

    @Test
    void testMockPlus() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(4, 3, 7);
        when(listMock.size()).thenReturn(2, 2, 2, 1);

        assertEquals(7, (int) calculator.calculate("3 4 +"));
    }

    @Test
    void testMockMinus() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(4, 3, -1);
        when(listMock.size()).thenReturn(2, 2, 2, 1);

        assertEquals(-1, (int) calculator.calculate("3 4 -"));
    }

    @Test
    void testMockMultiply() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(4, 3, 12);
        when(listMock.size()).thenReturn(2, 2, 2, 1);

        assertEquals(12, (int) calculator.calculate("3 4 *"));
    }

    @Test
    void testMockDivide() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(4, 20, 5);
        when(listMock.size()).thenReturn(2, 2, 2, 1);

        assertEquals(5, (int) calculator.calculate("20 4 /"));
    }
    
    @Test
    void testBigMock() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        when(listMock.get(anyInt())).thenReturn(4, 3, 5, -1, -5);
        when(listMock.size()).thenReturn(2, 2, 2, 2, 2, 2, 1);

        var calculator = new Calculator(listMock);

        assertEquals(-5, (int) calculator.calculate("3 4 - 5 *"));
    }

    @Test
    void testSpyPlus() {
        List<Integer> list = new LinkedList<>();
        List<Integer> spy = spy(list);

        InOrder inOrder = inOrder(spy);
        String expression = "7 6 +";

        Calculator calculator = new Calculator(spy);
        assertEquals(13, calculator.calculate(expression));

        inOrder.verify(spy).add(7);
        inOrder.verify(spy).add(6);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(13);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(0);
    }

    @Test
    void testSpyMinus() {
        List<Integer> list = new LinkedList<>();
        List<Integer> spy = spy(list);

        InOrder inOrder = inOrder(spy);
        String expression = "7 13 -";

        Calculator calculator = new Calculator(spy);
        assertEquals(-6, calculator.calculate(expression));

        inOrder.verify(spy).add(7);
        inOrder.verify(spy).add(13);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(-6);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(0);
    }

    @Test
    void testSpyMultiply() {
        List<Integer> list = new LinkedList<>();
        List<Integer> spy = spy(list);

        InOrder inOrder = inOrder(spy);
        String expression = "7 6 *";

        Calculator calculator = new Calculator(spy);
        assertEquals(42, calculator.calculate(expression));

        inOrder.verify(spy).add(7);
        inOrder.verify(spy).add(6);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(42);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(0);
    }

    @Test
    void testSpyDiv() {
        List<Integer> list = new LinkedList<>();
        List<Integer> spy = spy(list);

        InOrder inOrder = inOrder(spy);
        String expression = "42 4 /";

        Calculator calculator = new Calculator(spy);
        assertEquals(10, calculator.calculate(expression));

        inOrder.verify(spy).add(42);
        inOrder.verify(spy).add(4);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).size();
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).size();
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(10);
        inOrder.verify(spy, times(2)).size();
        inOrder.verify(spy).get(0);
    }

    @Test
    void testSpyDivByNull() {
        List<Integer> list = new LinkedList<>();
        List<Integer> spy = spy(list);

        InOrder inOrder = inOrder(spy);
        String expression = "42 0 /";

        Calculator calculator = new Calculator(spy);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(expression));

        inOrder.verify(spy).add(42);
        inOrder.verify(spy).add(0);
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).remove(0);
    }

    @Test
    void testIncorrectExpressionInfix() {
        String expression = "3 + 4";
        List<Integer> list = new LinkedList<>();
        Calculator calculator = new Calculator(list);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(expression));
    }

    @Test
    void testIncorrectExpressionDivByZero() {
        String expression = "3 0 /";
        List<Integer> list = new LinkedList<>();
        Calculator calculator = new Calculator(list);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(expression));
    }

    @Test
    void testIncorrectExpressionCanNotParseInt() {
        String expression = "a 4 +";
        List<Integer> list = new LinkedList<>();
        Calculator calculator = new Calculator(list);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(expression));
    }

    @Test
    void testIncorrectExpressionSmallSize() {
        String expression = "3 +";
        List<Integer> list = new LinkedList<>();
        Calculator calculator = new Calculator(list);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(expression));
    }

    @Test
    void testIncorrectExpressionBigSize() {
        String expression = "3 4 + 1 2 3 3 4 4 5 6 7";
        List<Integer> list = new LinkedList<>();
        Calculator calculator = new Calculator(list);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(expression));
    }

    @Test
    void testPriorityOperationsMockTest() {
        List<Integer> listMock = (List<Integer>) mock(List.class);
        // (5 + 6) * 4 = 44
        String expression = "5 6 + 4 *";

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(6, 5, 11, 44);
        when(listMock.size()).thenReturn(2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 1);

        assertEquals(44, calculator.calculate(expression));
    }
}