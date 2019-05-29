package ru.hse.surkov.pw16.task01.findPair;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.Optional;

public class Main extends Application {

    private static final double FPS = 60;
    private static final double SCREEN_WIDTH = Toolkit
            .getDefaultToolkit()
            .getScreenSize()
            .getWidth();
    private static final double SCREEN_HEIGHT = Toolkit
            .getDefaultToolkit()
            .getScreenSize()
            .getHeight();
    private static final double WINDOW_WIDTH = SCREEN_WIDTH * 0.3;
    private static final double WINDOW_HEIGHT = SCREEN_HEIGHT * 0.5;
    private static final double DIALOG_WINDOW_WIDTH = SCREEN_WIDTH * 0.15;
    private static final double DIALOG_WINDOW_HEIGHT = SCREEN_WIDTH * 0.05;
    private int boardSize = 4;

    volatile private Board board;
    private Button[][] buttons;
    private Timeline timeline;

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void createBoard() {
        board = new Board(boardSize);
    }

    private Stage mainStage;

    private void createUI(Stage primaryStage) {
        mainStage = primaryStage;
        var pane = new GridPane();

        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);

        for (int i = 0; i < board.getSize(); i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setFillHeight(true);
            rowConstraints.setVgrow(Priority.ALWAYS);
            rowConstraints.setPercentHeight(100.0 / boardSize);
            pane.getRowConstraints().add(rowConstraints);
        }

        for (int j = 0; j < board.getSize(); j++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setFillWidth(true);
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.setPercentWidth(100.0 / boardSize);
            pane.getColumnConstraints().add(columnConstraints);
        }

        buttons = new Button[board.getSize()][board.getSize()];

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Button button = new Button();
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                final int row = i;
                final int coloumn = j;
                buttons[i][j] = button;
                final int I = i;
                final int J = j;
                button.setOnAction(value -> {
                    board.makeTurn(I, J);
                    updateButtons();
                });
                pane.add(button, i, j, 1, 1);
            }
        }

        Scene scene = new Scene(pane,
                primaryStage.getWidth(),
                primaryStage.getHeight());

        primaryStage.setScene(scene);
    }

    private void finishGame() {
        mainStage.close();
        timeline.stop();
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Find Pair");
        alert.setHeaderText("The End");
        alert.setContentText("Congratulations! You Win!");
        alert.showAndWait();
        System.exit(0);
    }

    private void updateButtons() {
        if (board.getCatchedCellsNumber() == boardSize * boardSize) {
            finishGame();
        }
        board.unTouch();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Board.CellState cellState = board.getCellState(i, j);
                if (cellState.equals(Board.CellState.NOT_TOUCHED)) {
                    buttons[i][j].setDisable(false);
                    buttons[i][j].setText("");
                } else {
                    buttons[i][j].setDisable(true);
                    buttons[i][j].setText(Integer.toString(board.getValue(i, j)));
                }
            }
        }
    }

    private void createTimeLine() {
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        var keyFrame = new KeyFrame(
                Duration.seconds(1 / FPS),
                event -> updateButtons()
        );
        timeline.getKeyFrames().add(keyFrame);
    }

    private void createAskBoardSizeDialog() {
        var dialog = new TextInputDialog("4");
        dialog.setTitle("Find Pair");
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(DIALOG_WINDOW_WIDTH);
        dialog.getDialogPane().setMinHeight(DIALOG_WINDOW_HEIGHT);
        dialog.setHeaderText("Settings");
        dialog.setContentText("Please, enter a board size");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            System.exit(0);
        }
        try {
            boardSize = Integer.parseInt(result.get());
            board = new Board(boardSize);
        } catch (Exception ignored) {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Find Pair");
            alert.setHeaderText("Error!");
            alert.setContentText("Board size should be positive even integer!");
            alert.showAndWait();
            createAskBoardSizeDialog();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Find pair");
        createAskBoardSizeDialog();
        createUI(primaryStage);
        createTimeLine();
        timeline.play();
        primaryStage.show();
    }
}