package com.alex.utils;

import com.alex.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXUtils {

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
        stage.setTitle("sample title");
        stage.show();
        App.setStage(stage);
        return loader;
    }

    public static void popUp(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Ended");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
