package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.scholar.tasks.RetrieveResultsTask;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;


/**
 * Created by janscheurenbrand on 25.11.14.
 */
public class Controller {

    @FXML private MenuBar menubar;
    @FXML private Accordion accordion;
    @FXML private TitledPane searchPane;
    @FXML private CheckBox includePatentsCheckbox;
    @FXML private CheckBox includeCitationsCheckbox;
    @FXML private TextField resultsTextfield;
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

        resultsTextfield.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!"0123456789".contains(event.getCharacter())) event.consume();
        });

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

        int numberOfResults = Integer.parseInt(resultsTextfield.getText());

        if (!(numberOfResults > 0)) {
            numberOfResults = 100;
        }

        boolean includePatents = includePatentsCheckbox.isSelected();
        boolean includeCitations = includeCitationsCheckbox.isSelected();


        RetrieveResultsTask retrieveResultsTask = new RetrieveResultsTask(searchString, numberOfResults, includePatents, includeCitations);
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
