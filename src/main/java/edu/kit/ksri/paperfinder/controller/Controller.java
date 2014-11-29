package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.scholar.tasks.RetrieveResultsTask;
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

    @FXML private MenuBar menubar;
    @FXML private Accordion accordion;
    @FXML private TitledPane searchPane;
    @FXML private TitledPane filterPane;
    @FXML private TitledPane exportPane;
    @FXML private TextField searchText;
    @FXML private TableView resultsTableView;
    @FXML private TableColumn titleCol;
    @FXML private TableColumn authorCol;
    @FXML private TableColumn publicationCol;
    @FXML private TableColumn sourceCol;
    @FXML private TableColumn citationsCol;
    @FXML private TableColumn yearPublishedCol;
    @FXML private Label status;
    private ObservableList<Article> results;

    @FXML
    protected void initialize() {
        menubar.setUseSystemMenuBar(true);
        accordion.setExpandedPane(searchPane);

        Platform.runLater(searchText::requestFocus);

        titleCol.setCellValueFactory(new PropertyValueFactory<Article, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Article, String>("author"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<Article, String>("source"));
        publicationCol.setCellValueFactory(new PropertyValueFactory<Article, String>("publication"));
        yearPublishedCol.setCellValueFactory(new PropertyValueFactory<Article, Integer>("yearPublished"));
        citationsCol.setCellValueFactory(new PropertyValueFactory<Article, Integer>("citations"));
    }

    @FXML
    private void performSearch(ActionEvent event) {
        String searchString = searchText.getText();
        int numberOfResults = Config.DEFAULT_NUMBER_OF_RESULTS;

        RetrieveResultsTask retrieveResultsTask = new RetrieveResultsTask(searchString, numberOfResults);
        new Thread(retrieveResultsTask).start();
        results = retrieveResultsTask.getPartialResults();
        resultsTableView.setItems(results);
        status.textProperty().bind(retrieveResultsTask.messageProperty());
        retrieveResultsTask.setOnSucceeded(e -> accordion.setExpandedPane(filterPane));
    }

    @FXML
    private void performFilter(ActionEvent event) {

    }
}
