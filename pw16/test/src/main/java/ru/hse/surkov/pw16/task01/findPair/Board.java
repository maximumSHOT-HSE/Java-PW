package ru.hse.surkov.pw16.task01.findPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Storage/Engine for game board
 * with ability to make turn, get information
 * about the game state and updating game states.
 * */
public class Board {

    private static final long BUTTON_LIFE_TIME = 1_500;
    private int seed = 42;
    private Random random = new Random(seed);

    /**
     * State of cell
     * */
    public enum CellState {
        NOT_TOUCHED,
        TOUCHED,
        CATCHED
    }

    private int size;
    private Cell[][] cells;

    private int catchedCellsNumber = 0;
    private List<Pair> touchesStack = new ArrayList<>();

    private void generateValues() {
        List<Integer> randomValues = new ArrayList<>();
        int maxValue = size * size / 2;
        for (int i = 1; i <= maxValue; i++) {
            randomValues.add(i);
            randomValues.add(i);
        }
        Collections.shuffle(randomValues);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setValue(randomValues.get(i * size + j));
            }
        }
    }

    public Board(int size) {
        if (size <= 0 || size % 2 == 1) {
            throw new IllegalArgumentException(
                "size should be positive even integer, but found size = " + size
            );
        }
        this.size = size;
        cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();
            }
        }
        generateValues();
    }

    public CellState getCellState(int i, int j) {
        return cells[i][j].getState();
    }

    public int getSize() {
        return size;
    }

    /**
     * Checks correctness of the turn,
     * so player is not able to touch buttons too often.
     * Method does not check correctness of coordinates.
     * */
    public void makeTurn(int i, int j) {
        if (touchesStack.size() > 1 + getCatchedCellsNumber()) {
            return;
        }
        cells[i][j].setLastTouchedCellRow(System.currentTimeMillis());
        if (touchesStack.size() % 2 == 0) {
            cells[i][j].setState(CellState.TOUCHED);
            touchesStack.add(new Pair(i, j));
        } else {
            int lastTouchedCellRow = touchesStack.get(touchesStack.size() - 1).getFirst();
            int getLastTouchedCellColumn = touchesStack.get(touchesStack.size() - 1).getSecond();
            int lastTouchedValue = getValue(
                    lastTouchedCellRow,
                    getLastTouchedCellColumn
            );
            int currentTouchedValue = getValue(i, j);
            if (lastTouchedValue == currentTouchedValue) {
                catchedCellsNumber += 2;
                cells[lastTouchedCellRow][getLastTouchedCellColumn].setState(CellState.CATCHED);
                cells[i][j].setState(CellState.CATCHED);
                touchesStack.add(new Pair(i, j));
            } else {
                cells[i][j].setState(CellState.TOUCHED);
                touchesStack.add(new Pair(i, j));
            }
        }
    }

    public int getValue(int i, int j) {
        return cells[i][j].getValue();
    }

    public boolean isAlive(int i, int j) {
        return System.currentTimeMillis() - cells[i][j].getLastTouchedCellRow() <= BUTTON_LIFE_TIME;
    }

    /**
     * Removes from touch buttons stacks such buttons,
     * that was touched too long ago.
     * */
    public void unTouch() {
        while (!touchesStack.isEmpty()) {
            int lastTouchedCellRow = touchesStack.get(touchesStack.size() - 1).getFirst();
            int getLastTouchedCellColumn = touchesStack.get(touchesStack.size() - 1).getSecond();
            CellState state = getCellState(lastTouchedCellRow, getLastTouchedCellColumn);
            if (state.equals(CellState.CATCHED)) {
                break;
            }
            if (isAlive(lastTouchedCellRow, getLastTouchedCellColumn)) {
                break;
            }
            touchesStack.remove(touchesStack.size() - 1);
            cells[lastTouchedCellRow][getLastTouchedCellColumn].setState(CellState.NOT_TOUCHED);
        }
    }

    public int getCatchedCellsNumber() {
        return catchedCellsNumber;
    }

    private class Cell {
        private long lastTouchedCellRow = (long) 1e18;
        private CellState state = CellState.NOT_TOUCHED;
        private int value = 0;

        public long getLastTouchedCellRow() {
            return lastTouchedCellRow;
        }

        public void setLastTouchedCellRow(long lastTouchedCellRow) {
            this.lastTouchedCellRow = lastTouchedCellRow;
        }

        public CellState getState() {
            return state;
        }

        public void setState(CellState state) {
            this.state = state;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
