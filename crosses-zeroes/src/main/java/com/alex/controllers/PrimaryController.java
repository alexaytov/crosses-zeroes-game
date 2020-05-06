package com.alex.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.alex.App;
import com.alex.game.Game;
import com.alex.utils.JavaFXUtils;
import com.alex.utils.WindowsConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable{

    @FXML
    private VBox vBox;

    private Button[][] matrix;
    private Game game;
    private String empty = "0";
    private int elementMinWidth = 50;
    private int elementMinHeight = 30;

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

            updateView(game.getMatrix());

            int isGameEnded = game.isGameEnded(game.getMatrix());
            System.out.println(isGameEnded);
            if(isGameEnded == 2 || isGameEnded == 1){
                System.out.println("GAME ENDED " + game.isGameEnded(game.getMatrix()));
                Thread.sleep(1000);
                game.reset();
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
                    matrix[i][j].setStyle("-fx-background-color:#8c94ff; -fx-min-width: 50; -fx-min-height: 30;");
                    String firstPlayerSign = "X";
                    matrix[i][j].setText(firstPlayerSign);
                }

                if(currentMatrix[i][j] == 2){
                    matrix[i][j].setStyle("-fx-background-color:#fff799; -fx-min-width: 50; -fx-min-height: 30;");
                    String secondPlayerSign = "O";
                    matrix[i][j].setText(secondPlayerSign);
                }
            }
        }
    }

    @FXML
    private void goBack(){
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
        System.out.println("asd");
        App.exitGame();
    }

    void setGame(Game game){
        this.game = game;
        // ai makes the first turn
        updateView(game.getMatrix());
    }

    private void printMatrix(int[][] matrix){
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.printf("%d ", element);
            }
            System.out.println();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        matrix = new Button[15][15];
        initMatrix(matrix);
    }
}
