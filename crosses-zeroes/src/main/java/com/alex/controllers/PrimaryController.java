package com.alex.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.alex.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable {

    @FXML
    private HBox hbox;
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
    }

    private void initMatrix(Button[][] matrix){

        for (int i = 0; i < matrix.length; i++) {
            VBox vBox = new VBox();
            for (int j = 0; j < matrix[i].length; j++) {
                Button btn = new Button(empty);
                btn.setStyle("-fx-background-color: white; -fx-min-width: 50; -fx-border-color: black; -fx-min-height: 30;");
                int finalI = i;
                int finalJ = j;
                btn.setOnMouseClicked(mouseEvent -> buttonClicked(finalI, finalJ));
                matrix[i][j] = btn;
                vBox.getChildren().add(btn);
            }
            hbox.getChildren().add(vBox);
        }
        printMatrix(matrix);
    }


    private void printMatrix(Button[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j].getText() + " ");
            }
            System.out.println();
        }
    }

    private void buttonClicked(int x, int y){
        try {
            System.out.println(x + " " + y);
            game.setValue(x, y);
            updateView(game.getMatrix());
            System.out.println(game.isGameEnded());
        }catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void updateView(int[][] currentMatrix){
        for (int i = 0; i < currentMatrix.length; i++) {
            for (int j = 0; j < currentMatrix[i].length; j++) {
                if(currentMatrix[i][j] == 0){
                    matrix[i][j].setText(empty);
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
}
