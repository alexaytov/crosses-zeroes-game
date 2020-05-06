package com.alex;

import com.alex.utils.GlobalConstants;
import com.alex.utils.JavaFXUtils;
import com.alex.utils.WindowsConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.alex.utils.GlobalConstants.WINDWOS_TITLE;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("start"));
        stage.setScene(scene);
        stage.setTitle(WINDWOS_TITLE);
        stage.show();
        App.stage = stage;

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void exitGame() {
        stage.close();
    }

    public static void setStage(Stage stage){
        App.stage = stage;
    }


}