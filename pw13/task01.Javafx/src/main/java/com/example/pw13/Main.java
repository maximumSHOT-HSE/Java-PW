package com.example.pw13;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int SIZE = 3;
    private static final Pair[] shifts = new Pair[] {
            new Pair(0, 1),
            new Pair(1, 0),
            new Pair(1, 1),
            new Pair(1, -1)
    };

    public enum CellState {
        EMPTY, X, O
    }

    public enum GameState {
        IN_PROGRESS, DRAW, X_WIN, O_WIN
    }

    private CellState getOppositeCellState(CellState state) {
        return state.equals(CellState.X) ? CellState.O : state.equals(CellState.O) ? CellState.X : CellState.EMPTY;
    }

    private CellState[][] matrix = new CellState[SIZE][SIZE];
    private CellState currentTurn = CellState.X;
    GameState gameState = GameState.IN_PROGRESS;

    public GameState getGameState() {
        return gameState;
    }

    public Main() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = CellState.EMPTY;
            }
        }
    }

    /**
     * Method for testing.
     * Tries to change cell state and in case of success returns true.
     * Otherwise does nothing and returns false
     *
     * @throws IllegalArgumentException
     * */
    public boolean setCellState(int i, int j, CellState state) {
        if (!state.equals(currentTurn)) {
            throw new IllegalArgumentException("Invalid state");
        }
        if (i < 0 || i >= SIZE || j < 0 || j >= SIZE) {
            throw new IllegalArgumentException("Coordinates out of bounds (" + i + ", " + j + ")");
        }
        if (!matrix[i][j].equals(CellState.EMPTY)) {
            return false;
        }
        matrix[i][j] = state;
        currentTurn = getOppositeCellState(currentTurn);
        return true;
    }

    private void process(int row, int coloumn, Button button) {
        if (!gameState.equals(GameState.IN_PROGRESS)) {
            return;
        }
        if (matrix[row][coloumn].equals(CellState.EMPTY)) {
            matrix[row][coloumn] = currentTurn;
            button.setText(currentTurn.toString());
            if (isEnd()) {
                button.setText(currentTurn.toString() + " WIN");
                gameState = currentTurn.equals(CellState.X) ? GameState.X_WIN : GameState.O_WIN;
            } else if (!anyEmptyCellExists()) {
                button.setText(currentTurn.toString() + " DRAW");
                gameState = GameState.DRAW;
            }
            currentTurn = getOppositeCellState(currentTurn);
        }
    }

    private boolean anyEmptyCellExists() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i][j].equals(CellState.EMPTY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isEnd() {
        for (Pair shift : shifts) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (matrix[i][j].equals(CellState.EMPTY)) {
                        continue;
                    }
                    int deltaI = shift.getX();
                    int deltaJ = shift.getY();
                    boolean allSame = true;
                    for (int q = 0; q < SIZE; q++) {
                        int curI = i + deltaI * q;
                        int curJ = j + deltaJ * q;
                        if (curI < 0 || curI >= SIZE || curJ < 0 || curJ >= SIZE) {
                            allSame = false;
                            break;
                        }
                        if (!matrix[curI][curJ].equals(matrix[i][j])) {
                            allSame = false;
                            break;
                        }
                    }
                    if (allSame) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tic Tac Toe");

        var pane = new GridPane();

        for (int i = 0; i < SIZE; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setFillHeight(true);
            rowConstraints.setVgrow(Priority.ALWAYS);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int j = 0; j < SIZE; j++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setFillWidth(true);
            columnConstraints.setHgrow(Priority.ALWAYS);
            pane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button button = new Button();
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                final int row = i;
                final int coloumn = j;
                button.setOnAction(value -> {
                    process(row, coloumn, button);
                });
                pane.add(button, i, j, 1, 1);
            }
        }

        Scene scene = new Scene(pane, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
