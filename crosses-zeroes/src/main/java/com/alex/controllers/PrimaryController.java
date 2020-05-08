package com.alex.controllers;

import com.alex.game.Game;
import com.alex.utils.JavaFXUtils;
import com.alex.utils.WindowsConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    private VBox vBox;

    private Button[][] matrix;
    private Game game;
    private String emptySign = "0";

    private void initMatrix(Button[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            HBox hbox = new HBox();
            for (int j = 0; j < matrix[i].length; j++) {
                Button btn = new Button(emptySign);
                btn.setStyle("-fx-background-color: white; -fx-min-width: 50; -fx-border-color: black; -fx-min-height: 30;");
                int finalI = i;
                int finalJ = j;
                btn.setOnMouseClicked(mouseEvent -> buttonClicked(finalI, finalJ));
                matrix[i][j] = btn;
                hbox.getChildren().add(btn);
            }
            vBox.getChildren().add(hbox);
        }
    }


    private void buttonClicked(int x, int y) {
        try {
            int isGameEnded = game.setValue(x, y);
            updateView(game.getMatrix());

            if (isGameEnded != -1) {
                List<Integer> winnerElements = game.getWinnerElements();
                for (int i = 0; i < winnerElements.size() - 1; i += 2) {
                    matrix[winnerElements.get(i)][winnerElements.get(i + 1)].setStyle("-fx-background-color:  #ff726b; -fx-min-width: 50; -fx-min-height: 30;");
                }

                if (isGameEnded == 1) {
                    JavaFXUtils.popUp("X player wins");
                }

                if (isGameEnded == 2) {
                    JavaFXUtils.popUp("O player wins");

                }

                if (isGameEnded == 0) {
                    JavaFXUtils.popUp("Tie");
                }
                game.reset();
            }
            updateView(game.getMatrix());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void updateView(int[][] currentMatrix) {
        for (int i = 0; i < currentMatrix.length; i++) {
            for (int j = 0; j < currentMatrix[i].length; j++) {
                if (currentMatrix[i][j] == 0) {
                    matrix[i][j].setText(emptySign);
                    matrix[i][j].setStyle("-fx-background-color: white; -fx-min-width: 50; -fx-min-height: 30;");
                }

                if (currentMatrix[i][j] == 1) {
                    matrix[i][j].setStyle("-fx-background-color:#8c94ff; -fx-min-width: 50; -fx-min-height: 30;");
                    String firstPlayerSign = "X";
                    matrix[i][j].setText(firstPlayerSign);
                }

                if (currentMatrix[i][j] == 2) {
                    matrix[i][j].setStyle("-fx-background-color:#fff799; -fx-min-width: 50; -fx-min-height: 30;");
                    String secondPlayerSign = "O";
                    matrix[i][j].setText(secondPlayerSign);
                }
            }
        }
    }

    @FXML
    private void goBack() {
        JavaFXUtils.loadWindow(WindowsConstants.START_PATH, WindowsConstants.START_WIDTH, WindowsConstants.START_HEIGHT);
        this.vBox.getScene().getWindow().hide();
    }

    @FXML
    private void resetGame() {
        game.reset();
        updateView(game.getMatrix());
    }

    @FXML
    private void exitGame() {
        JavaFXUtils.exitGame();
    }

    void setGame(Game game) {
        this.game = game;
        // ai makes the first turn
        updateView(game.getMatrix());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        matrix = new Button[15][15];
        initMatrix(matrix);
    }
}