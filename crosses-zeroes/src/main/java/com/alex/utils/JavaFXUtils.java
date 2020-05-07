package com.alex.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

import static com.alex.utils.GlobalConstants.WINDWOS_TITLE;

public class JavaFXUtils {

    private static Stage stage;

    public static FXMLLoader loadWindow(String fxmlPath, int width, int height) {
        FXMLLoader loader = new FXMLLoader(JavaFXUtils.class.getResource(fxmlPath));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Root could not load");
        }
        Scene scene = new Scene(root, width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(WINDWOS_TITLE);
        stage.show();
        JavaFXUtils.stage = stage;
        return loader;
    }

    public static void exitGame() {
        stage.close();
    }

    public static void popUp(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Ended");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}