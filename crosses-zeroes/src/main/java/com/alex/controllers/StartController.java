package com.alex.controllers;

import com.alex.App;
import com.alex.game.Game;
import com.alex.utils.JavaFXUtils;
import com.alex.utils.WindowsConstants;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private RadioButton xSign;

    @FXML
    private JFXToggleButton aiToggleButton;

    @FXML
    private RadioButton aiXSign;

    @FXML
    private void startGame() {
        xSign.getScene().getWindow().hide();
        FXMLLoader loader = JavaFXUtils.loadWindow(WindowsConstants.PRIMARY_PATH, WindowsConstants.PRIMARY_WIDTH, WindowsConstants.PRIMARY_HEIGHT);
        PrimaryController primaryController = loader.getController();
        primaryController.setGame(initGame());
    }

    private Game initGame() {
        return new Game(15, 15, aiToggleButton.isSelected(), xSign.isSelected(), aiXSign.isSelected());
    }

    @FXML
    private void exitGame() {
        JavaFXUtils.exitGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xSign.setSelected(true);
        aiToggleButton.setSelected(true);
        aiXSign.setSelected(true);
    }
}