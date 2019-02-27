package com.example.hse;

import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.spy;

class CalculatorTest {

    private List list;
    private List spy;

    @BeforeEach
    void setUp() {
        list = new LinkedList();
        spy = spy(list);
    }
}