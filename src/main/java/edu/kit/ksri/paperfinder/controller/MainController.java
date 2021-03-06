package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.Main;
import edu.kit.ksri.paperfinder.export.ExcelExport;
import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.scholar.tasks.RetrieveResultsTask;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static edu.kit.ksri.paperfinder.util.NumberUtils.parseInt;

/**
 * Created by janscheurenbrand on 25.11.14.
 */
public class MainController {
    Main main;
    ResourceBundle resourceBundle;

    @FXML private BorderPane root;
    @FXML private MenuBar menubar;
    @FXML private Accordion accordion;
    @FXML private TitledPane searchPane;
    @FXML private CheckBox includePatentsCheckbox;
    @FXML private CheckBox includeCitationsCheckbox;
    @FXML private TextField firstResultsTextfield;
    @FXML private RadioButton firstResultsRadio;
    @FXML private RadioButton allResultsRadio;
    @FXML private CheckBox appendResultsCheckbox;
    @FXML private TitledPane filterPane;
    @FXML private TextField citationsLowTextfield;
    @FXML private TextField citationsHighTextfield;
    @FXML private TextField publishedYearLowTextfield;
    @FXML private TextField publishedYearHighTextfield;
    @FXML private CheckBox onlyPDFCheckbox;
    @FXML private TitledPane exportPane;
    @FXML private CheckBox exportOnlySelectedCheckbox;
    @FXML private TextField searchText;
    @FXML private TableView resultsTableView;
    @FXML private TableColumn selectedCol;
    @FXML private TableColumn titleCol;
    @FXML private TableColumn authorCol;
    @FXML private TableColumn publicationCol;
    @FXML private TableColumn sourceCol;
    @FXML private TableColumn citationsCol;
    @FXML private TableColumn yearPublishedCol;
    @FXML private VBox singleArticle;
    @FXML private SingleArticleController singleArticleController;
    @FXML private Label status;
    @FXML private Menu windowsMenu;
    @FXML private MenuItem windowsMenuSeperator;
    @FXML private MenuItem downloadsMenuItem;

    private ObservableList<Article> results;
    private List<Article> completeResultList = new ArrayList<>();

    private DownloadController downloadController = new DownloadController();

    @FXML
    protected void initialize() {
        resourceBundle = ResourceBundle.getBundle("paperfinder");
        menubar.setUseSystemMenuBar(true);
        firstResultsTextfield.setText(String.valueOf(Config.DEFAULT_NUMBER_OF_RESULTS));
        firstResultsTextfield.disableProperty().bind(firstResultsRadio.selectedProperty().not());
        accordion.setExpandedPane(searchPane);
        Platform.runLater(searchText::requestFocus);

        makeNumberField(firstResultsTextfield);
        makeNumberField(citationsLowTextfield);
        makeNumberField(citationsHighTextfield);
        makeNumberField(publishedYearLowTextfield);
        makeNumberField(publishedYearHighTextfield);

        selectedCol.setCellValueFactory(new PropertyValueFactory<Article, Boolean>("selected"));
        selectedCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectedCol));
        titleCol.setCellValueFactory(new PropertyValueFactory<Article, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<Article, String>("author"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<Article, String>("source"));
        publicationCol.setCellValueFactory(new PropertyValueFactory<Article, String>("publication"));
        yearPublishedCol.setCellValueFactory(new PropertyValueFactory<Article, Integer>("yearPublished"));
        citationsCol.setCellValueFactory(new PropertyValueFactory<Article, Integer>("citations"));

        resultsTableView.getSelectionModel().selectedItemProperty().addListener(this::onSelection);
        resultsTableView.setOnKeyPressed(this::handleKeyboardShortcut);

        singleArticleController.setMainController(this);
        singleArticle.setVisible(false);
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
        rebuildWindowMenu();
    }

    @FXML
    private void performSearch(ActionEvent event) {
        String searchString = searchText.getText();

        int numberOfResults = Integer.parseInt(firstResultsTextfield.getText());
        if (!(numberOfResults > 0)) numberOfResults = Config.DEFAULT_NUMBER_OF_RESULTS;
        if (allResultsRadio.isSelected()) {
            numberOfResults = Integer.MAX_VALUE;
        }

        boolean includePatents = includePatentsCheckbox.isSelected();
        boolean includeCitations = includeCitationsCheckbox.isSelected();

        RetrieveResultsTask retrieveResultsTask = new RetrieveResultsTask(searchString, numberOfResults, includePatents, includeCitations);
        new Thread(retrieveResultsTask).start();

        if (appendResultsCheckbox.isSelected()) {
            results.addAll(retrieveResultsTask.getPartialResults());
        } else {
            results = retrieveResultsTask.getPartialResults();
        }

        resultsTableView.setItems(results);
        status.textProperty().bind(retrieveResultsTask.messageProperty());
        retrieveResultsTask.setOnSucceeded(this::afterSearch);
        retrieveResultsTask.setOnFailed(this::afterFailedSearch);

    }

    private void afterSearch(WorkerStateEvent workerStateEvent) {
        status.textProperty().unbind();
        accordion.setExpandedPane(filterPane);
        if (results.size() > 0) {
            singleArticle.setVisible(true);
            singleArticleController.setArticle(results.get(0));
            completeResultList.addAll(results);
        }
        status.setText(String.format(t("status.loadedItems"), results.size()));
    }

    private void afterFailedSearch(WorkerStateEvent workerStateEvent) {
        status.textProperty().unbind();
        if (results.size() > 0) {
            singleArticle.setVisible(true);
            singleArticleController.setArticle(results.get(0));
            completeResultList.addAll(results);
        }
    }

    @FXML
    private void performFilter(ActionEvent event) {
        this.results.clear();
        this.results.addAll(completeResultList
                .stream()
                .filter(article -> article.getCitations() >= parseInt(citationsLowTextfield.getText(), 0))
                .filter(article -> article.getCitations() <= parseInt(citationsHighTextfield.getText(), Integer.MAX_VALUE))
                .filter(article -> article.getYearPublished() >= parseInt(publishedYearLowTextfield.getText(), 0))
                .filter(article -> article.getYearPublished() <= parseInt(publishedYearHighTextfield.getText(), Integer.MAX_VALUE))
                .filter(article -> (onlyPDFCheckbox.isSelected() && article.hasPDF()) || (!onlyPDFCheckbox.isSelected()))
                .collect(Collectors.toList()));
        singleArticleController.setArticle(results.get(0));
    }

    @FXML
    private void performExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle(t("export"));
        fileChooser.setInitialFileName(searchText.getText().replaceAll("[^a-zA-Z0-9.-]", "_")+".xls");
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (file != null) {
            ExcelExport excelExport = new ExcelExport(getExportList(), file);
            excelExport.export();
            status.setText(t("status.exportComplete"));
        }
    }

    private ObservableList<Article> getExportList() {
        ObservableList<Article> toExport = FXCollections.observableArrayList(new ArrayList());
        toExport.addAll(results);

        if (exportOnlySelectedCheckbox.isSelected()) {
            toExport = toExport.filtered(article -> article.getSelected());
        }
        return toExport;
    }

    @FXML
    private void performDownload(ActionEvent event) {
        getExportList().forEach(downloadController::download);
    }

    private void handleKeyboardShortcut(KeyEvent event) {
        Article selectedArticle = singleArticleController.getArticle();
        if (selectedArticle != null) {
            switch (event.getCode().getName()) {
                case "Space":
                    selectedArticle.toggleSelected();
                    break;
                default:
                    break;
            }
        }
    }

    private void onSelection(ObservableValue observableValue, Object oldValue, Object newValue) {
        if (resultsTableView.getSelectionModel().getSelectedItem() != null) {
            Article selectedArticle = (Article) newValue;
            singleArticleController.setArticle(selectedArticle);
        }
    }

    public String t(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return "%"+key;
        }
    }

    private void makeNumberField(TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!"0123456789".contains(event.getCharacter())) event.consume();
        });
    }

    public static void openWebpage(String uriString) {
        Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI(uriString);
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @FXML
    private void showCitationsGraph() {
        showGraph(GraphController.ChartMode.CITATIONS);
    }

    @FXML
    private void showCitationsCumulativeGraph() {
        showGraph(GraphController.ChartMode.CITATIONS_CUMULATIVE);
    }

    @FXML
    private void showPublishedGraph() {
        showGraph(GraphController.ChartMode.PUBLISHED);
    }

    @FXML
    private void showPublishedCumulativeGraph() {
        showGraph(GraphController.ChartMode.PUBLISHED_CUMULATIVE);
    }

    @FXML
    private void showDownloads() {
        downloadController.showDownloadManager();
    }

    @FXML
    private void openNewWindow() {
        main.openNewWindow();
    }

    @FXML
    private void closeCurrentWindow() {

    }

    @FXML
    private void showAbout() {

    }

    /**
     * Starts a new Scene and Stage to display a graphical visualization of different attributes of previously returned
     * articles.
     * @param chartMode
     */
    private void showGraph(GraphController.ChartMode chartMode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/graph.fxml"));
            loader.setResources(resourceBundle);
            Pane graphRoot = loader.load();
            GraphController graphController = (GraphController) loader.getController();
            Scene scene = new Scene(graphRoot, 600, 400);
            Stage graphStage = new Stage();
            graphStage.setTitle(t("graph.title." + chartMode.toString().toLowerCase()));
            graphStage.setScene(scene);
            graphController.setArticleList(completeResultList);
            graphController.setChartMode(chartMode);
            graphStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DownloadController getDownloadController() {
        return downloadController;
    }

    public void setDownloadController(DownloadController downloadController) {
        this.downloadController = downloadController;
        this.downloadController.setMainController(this);
    }


    public void rebuildWindowMenu() {
        windowsMenu.getItems().clear();
        main.getMainControllers().forEach(mc -> {
            windowsMenu.getItems().add(new MenuItem("test"));
        });
        windowsMenu.getItems().add(windowsMenuSeperator);
        windowsMenu.getItems().add(downloadsMenuItem);
    }

    public BorderPane getRoot() {
        return root;
    }
}
