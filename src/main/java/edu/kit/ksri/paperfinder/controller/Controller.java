package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Paper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Created by janscheurenbrand on 25.11.14.
 */
public class Controller {

    @FXML
    private Accordion accordion;
    @FXML private TitledPane searchPane;
    @FXML private TitledPane filterPane;
    @FXML private TitledPane exportPane;
    @FXML private TextField searchText;
    @FXML private TableView resultsTableView;
    @FXML private TableColumn titleCol;
    @FXML private TableColumn authorCol;
    @FXML private TableColumn citationsCol;
    @FXML private Label status;
    private ObservableList<Paper> results;

    @FXML
    protected void initialize() {
        accordion.setExpandedPane(searchPane);

        Platform.runLater(() -> searchText.requestFocus());

        titleCol.setCellValueFactory(new PropertyValueFactory<Paper, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Paper, String>("author"));
        citationsCol.setCellValueFactory(new PropertyValueFactory<Paper, Integer>("citations"));

    }

    @FXML
    private void performSearch(ActionEvent event) {
        String searchString = searchText.getText();

        PartialResultsTask partialResultsTask = new PartialResultsTask(searchString);
        new Thread(partialResultsTask).start();
        results = partialResultsTask.getPartialResults();
        resultsTableView.setItems(results);
        status.textProperty().bind(partialResultsTask.messageProperty());

        partialResultsTask.setOnSucceeded(e -> accordion.setExpandedPane(filterPane));
    }

    @FXML
    private void performFilter(ActionEvent event) {

    }
}
