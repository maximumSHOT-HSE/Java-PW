package ru.hse.surkov.pw16.task01.findPair;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @Test
    void testCorrectArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> board = new Board(-1));
        assertThrows(IllegalArgumentException.class,
                () -> board = new Board(3));
        assertDoesNotThrow(() -> board = new Board(100));
    }

    @Test
    void testCorrectGenerating() {
        int size = 50;
        int maxValue = size * size / 2;
        board = new Board(size);
        int[] count = new int[maxValue + 1];
        Arrays.fill(count, 0);
        assertEquals(size, board.getSize());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = board.getValue(i, j);
                assertTrue(1 <= value);
                assertTrue(value <= maxValue);
                assertTrue(count[value] < 2);
                count[value]++;
            }
        }
    }

    @Test
    void testNotCatchedNumber() {
        int size = 50;
        int maxValue = size * size / 2;
        board = new Board(size);
        Pair[] firstPosition = new Pair[maxValue + 1];
        Pair[] secondPosition = new Pair[maxValue + 1];
        Arrays.fill(firstPosition, null);
        Arrays.fill(secondPosition, null);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = board.getValue(i, j);
                if (firstPosition[value] == null) {
                    firstPosition[value] = new Pair(i, j);
                } else {
                    secondPosition[value] = new Pair(i, j);
                }
            }
        }
        for (int x = 1; x <= maxValue; x++) {
            for (int y = x + 1; y <= maxValue; y++) {
                board.makeTurn(firstPosition[x].getFirst(), firstPosition[x].getSecond());
                board.makeTurn(firstPosition[y].getFirst(), firstPosition[y].getSecond());
                assertEquals(0, board.getCatchedCellsNumber());
            }
        }
        for (int x = 1; x <= maxValue; x++) {
            board.makeTurn(firstPosition[x].getFirst(), firstPosition[x].getSecond());
            board.makeTurn(secondPosition[x].getFirst(), secondPosition[x].getSecond());
            assertEquals(2 * x, board.getCatchedCellsNumber());
        }
    }

    @Test
    void testCatchedNumber() {
        int size = 50;
        int maxValue = size * size / 2;
        board = new Board(size);
        Pair[] firstPosition = new Pair[maxValue + 1];
        Pair[] secondPosition = new Pair[maxValue + 1];
        Arrays.fill(firstPosition, null);
        Arrays.fill(secondPosition, null);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = board.getValue(i, j);
                if (firstPosition[value] == null) {
                    firstPosition[value] = new Pair(i, j);
                } else {
                    secondPosition[value] = new Pair(i, j);
                }
            }
        }
        for (int x = 1; x <= maxValue; x++) {
            board.makeTurn(firstPosition[x].getFirst(), firstPosition[x].getSecond());
            board.makeTurn(secondPosition[x].getFirst(), secondPosition[x].getSecond());
            assertEquals(2 * x, board.getCatchedCellsNumber());
        }
    }
}