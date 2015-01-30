package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.model.Download;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * Created by janscheurenbrand on 28.01.15.
 */
public class DownloadController {
    @FXML private Pane downloadsWrap;
    @FXML private ListView downloadsList;
    private ObservableList<Download> downloads =  FXCollections.observableArrayList(new ArrayList());
    private MainController mainController;


    @FXML
    private void initialize() {
        downloadsList.setItems(downloads);
        downloadsList.setCellFactory(param -> new DownloadCell());
        downloadsList.setFixedCellSize(40);
        downloadsList.setFocusTraversable( false );
        downloadsList.getSelectionModel().selectedIndexProperty().addListener((observable, oldvalue, newValue) -> Platform.runLater(() -> downloadsList.getSelectionModel().select(-1)));
    }

    public void download(Article article) {
        System.out.println("Download" + article);
        downloads.add(0, new Download(article));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }

    class DownloadCell extends ListCell<Download> {

        @Override
        public void updateItem(Download item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                System.out.println("update panel for download "+item.getTitle());
                GridPane gridPane = new GridPane();
                gridPane.setPadding(new Insets(5,5,5,5));
                gridPane.setHgap(10.0);
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHgrow(Priority.SOMETIMES);
                column1.setPrefWidth(300);
                ColumnConstraints column2 = new ColumnConstraints();
                column2.setHgrow(Priority.NEVER);
                gridPane.getColumnConstraints().addAll(column1, column2);

                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER_LEFT);

                Label titleLabel = new Label();
                titleLabel.setFont(new Font("System Bold", 13.0));
                titleLabel.setText(item.getTitle());
                vBox.getChildren().add(titleLabel);

                Label authorLabel = new Label();
                authorLabel.setText(item.getAuthor());
                vBox.getChildren().add(authorLabel);

                gridPane.add(vBox, 0, 0);

                Button actionButton = new Button();
                actionButton.setText(mainController.t(String.format("download.status.%s", item.statusProperty().intValue())));
                actionButton.setDisable(item.getStatus() != Download.COMPLETE);
                actionButton.setOnAction(item::handleAction);
                gridPane.add(actionButton,1,0);
                setGraphic(gridPane);

                item.statusProperty().addListener((observable, oldValue, newValue) -> {
                    actionButton.setText(mainController.t(String.format("download.status.%s", item.statusProperty().intValue())));
                    actionButton.setDisable(item.getStatus() != Download.COMPLETE);
                });
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