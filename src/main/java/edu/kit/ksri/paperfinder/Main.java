package edu.kit.ksri.paperfinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {
    ResourceBundle resourceBundle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        resourceBundle = ResourceBundle.getBundle("paperfinder");
        Pane root = FXMLLoader.load(getClass().getResource("/scene.fxml"), resourceBundle);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle(resourceBundle.getString("app.name"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}