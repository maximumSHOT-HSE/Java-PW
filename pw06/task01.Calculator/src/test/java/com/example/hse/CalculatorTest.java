package com.example.hse;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

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
}