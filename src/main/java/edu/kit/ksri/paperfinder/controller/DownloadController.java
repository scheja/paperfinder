package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.model.Download;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by janscheurenbrand on 28.01.15.
 */
public class DownloadController {
    @FXML private Pane downloadsWrap;
    @FXML private TableView downloadsTable;
    @FXML private TableColumn nameCol;
    @FXML private TableColumn statusCol;
    @FXML private Label downloadsPathLabel;
    private ObservableList<Download> downloads =  FXCollections.observableArrayList(new ArrayList());
    private MainController mainController;

    private Scene scene;
    private Stage stage;


    @FXML
    private void initialize() {
        downloadsTable.setItems(downloads);
        nameCol.setCellFactory(param -> new NameCell());
        statusCol.setCellFactory(param -> new StatusCell());
        downloadsPathLabel.setText("Download Location: " + Config.CONFIG_PATH);
        downloadsTable.setFixedCellSize(40);
        downloadsTable.setFocusTraversable( false );
        downloadsTable.getSelectionModel().selectedIndexProperty().addListener((observable, oldvalue, newValue) -> Platform.runLater(() -> downloadsTable.getSelectionModel().select(-1)));
    }

    public void download(Article article) {
        downloads.add(0, new Download(article));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void showDownloadManager() {
        if (scene == null || stage == null) {
            scene = new Scene(downloadsWrap, 500, 400);
            stage = new Stage();
            stage.setTitle("Downloads");
            stage.setScene(scene);
            stage.show();
        } else {
            stage.show();
            stage.toFront();
        }
    }

    class StatusCell extends TableCell<TableView,Download> {

        @Override
        public void updateItem(Download item, boolean empty) {
            super.updateItem(item, empty);
            item = (Download) getTableRow().getItem();
            if (item != null && !empty) {
                GridPane gridPane = new GridPane();
                gridPane.setPadding(new Insets(5, 5, 5, 5));
                Button actionButton = new Button();
                actionButton.setText(mainController.t(String.format("download.status.%s", item.statusProperty().intValue())));
                actionButton.setDisable(item.getStatus() != Download.COMPLETE);
                actionButton.setOnAction(item::handleAction);
                gridPane.add(actionButton,0,0);
                setGraphic(gridPane);

                final Download finalItem = item;
                item.statusProperty().addListener((observable, oldValue, newValue) -> {
                    actionButton.setText(mainController.t(String.format("download.status.%s", finalItem.statusProperty().intValue())));
                    actionButton.setDisable(finalItem.getStatus() != Download.COMPLETE);
                });
            } else {
                setText(null);
                setGraphic(null);
            }
        }

    }

    class NameCell extends TableCell<TableView,Download> {

        @Override
        public void updateItem(Download item, boolean empty) {
            super.updateItem(item, empty);
            item = (Download) getTableRow().getItem();
            if (item != null && !empty) {

                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER_LEFT);

                Label titleLabel = new Label();
                titleLabel.setFont(new Font("System Bold", 13.0));
                titleLabel.setText(item.getTitle());
                vBox.getChildren().add(titleLabel);

                Label authorLabel = new Label();
                authorLabel.setText(item.getAuthor());
                vBox.getChildren().add(authorLabel);

                setGraphic(vBox);
            } else {
                setText(null);
                setGraphic(null);
            }
        }

    }



    public Pane getDownloadsWrap() {
        return downloadsWrap;
    }

}