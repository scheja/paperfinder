package edu.kit.ksri.paperfinder;

import edu.kit.ksri.paperfinder.controller.DownloadController;
import edu.kit.ksri.paperfinder.controller.MainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Main extends Application {
    ResourceBundle resourceBundle;
    ObservableList<MainController> mainControllers = FXCollections.observableArrayList(new ArrayList());
    DownloadController downloadController = null;
    FXMLLoader loader = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.resourceBundle = ResourceBundle.getBundle("paperfinder");
        this.loader = new FXMLLoader(getClass().getResource("/scene.fxml"));
        this.loader.setResources(resourceBundle);

        MainController mainController = openNewWindow(primaryStage);

        FXMLLoader downloadsLoader = new FXMLLoader(getClass().getResource("/downloads.fxml"));
        downloadsLoader.setResources(resourceBundle);
        downloadsLoader.load();
        this.downloadController = downloadsLoader.getController();
        mainController.setDownloadController(downloadController);

        mainControllers.addListener((ListChangeListener<MainController>) c -> {
            mainControllers.stream().forEach(MainController::rebuildWindowMenu);
        });
    }

    public MainController openNewWindow() {
        Stage stage = new Stage();
        return openNewWindow(stage);
    }

    private MainController openNewWindow(Stage stage) {
        Pane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController mainController = loader.getController();
        mainControllers.add(mainController);
        mainController.setMain(this);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        decorateStage(stage);
        if (this.downloadController != null) {
            mainController.setDownloadController(downloadController);
        }
        return mainController;
    }

    private Stage decorateStage(Stage stage) {
        stage.setTitle(resourceBundle.getString("app.name"));
        stage.setMinHeight(625);
        stage.setMinWidth(800);
        stage.show();
        return stage;
    }

    public ObservableList<MainController> getMainControllers() {
        return mainControllers;
    }

    public DownloadController getDownloadController() {
        return downloadController;
    }
}