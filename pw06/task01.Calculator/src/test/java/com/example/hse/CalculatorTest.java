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
    void testMockPlus() {
        List<Integer> listMock = (List<Integer>) mock(List.class);

        var calculator = new Calculator(listMock);

        when(listMock.get(anyInt())).thenReturn(3, 4, 7);
        when(listMock.size()).thenReturn(2, 1);

        assertEquals(7, (int) calculator.calculate("3 4 +"));
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
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(13);
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
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(-6);
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
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(42);
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
        inOrder.verify(spy).get(1);
        inOrder.verify(spy).remove(1);
        inOrder.verify(spy).get(0);
        inOrder.verify(spy).remove(0);
        inOrder.verify(spy).add(10);
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
}