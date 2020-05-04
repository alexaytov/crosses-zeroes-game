package com.alex.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.alex.game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable {

    @FXML
    private VBox vBox;

    private Button[][] matrix;
    private Game game;
    private String firstPlayerSign = "X";
    private String secondPlayerSign = "O";
    private String empty = "0";
    private int elementMinWidth = 50;
    private int elementMinHeight = 30;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        matrix = new Button[15][15];
        initMatrix(matrix);
        game = new Game(15, 15);

        // ai makes the first turn
        game.nextTurn();
        updateView(game.getMatrix());
    }

    private void initMatrix(Button[][] matrix){

        for (int i = 0; i < matrix.length; i++) {
            HBox hbox = new HBox();
            for (int j = 0; j < matrix[i].length; j++) {
                Button btn = new Button(empty);
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


    private void printMatrix(Button[][] matrix){
        for (Button[] row : matrix) {
            for (Button col : row) {
                System.out.print(col.getText() + " ");
            }
            System.out.println();
        }
    }

    private void buttonClicked(int x, int y){
        try {
            game.setValue(x, y);
            updateView(game.getMatrix());

            System.out.println();
            game.nextTurn();
            updateView(game.getMatrix());

            int isGameEnded = game.isGameEnded(game.getMatrix());
            System.out.println(isGameEnded);
            if(isGameEnded == 2 || isGameEnded == 1){
                System.out.println("GAME ENDED " + game.isGameEnded(game.getMatrix()));
                Thread.sleep(1000);
                game.reset();
                game.nextTurn();
                updateView(game.getMatrix());
            }
        }catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateView(int[][] currentMatrix){
        for (int i = 0; i < currentMatrix.length; i++) {
            for (int j = 0; j < currentMatrix[i].length; j++) {
                if(currentMatrix[i][j] == 0){
                    matrix[i][j].setText(empty);
                    matrix[i][j].setStyle("-fx-background-color: white; -fx-min-width: 50; -fx-min-height: 30;");
                }

                if(currentMatrix[i][j] == 1){
                    matrix[i][j].setStyle("-fx-background-color:green; -fx-min-width: 50; -fx-min-height: 30;");
                    matrix[i][j].setText(firstPlayerSign);
                }

                if(currentMatrix[i][j] == 2){
                    matrix[i][j].setStyle("-fx-background-color:yellow; -fx-min-width: 50; -fx-min-height: 30;");
                    matrix[i][j].setText(secondPlayerSign);
                }
            }
        }
    }

    private void printMatrix(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%d ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
