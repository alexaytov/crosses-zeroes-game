package com.alex;

import com.alex.utils.JavaFXUtils;
import com.alex.utils.WindowsConstants;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        JavaFXUtils.loadWindow(WindowsConstants.START_PATH, WindowsConstants.START_WIDTH, WindowsConstants.START_HEIGHT);
    }
}