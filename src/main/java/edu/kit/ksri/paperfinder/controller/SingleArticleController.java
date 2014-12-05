package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Article;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by janscheurenbrand on 25.11.14.
 */
public class SingleArticleController {

    private Article article;
    @FXML private Label authorLabel;
    @FXML private Label titleLabel;
    @FXML private Label abstractLabel;
    @FXML private ToggleButton toggleSelectButton;
    @FXML private Button downloadButton;

    @FXML private Label status;
    private ObservableList<Article> results;
    private List<Article> completeResultList = new ArrayList<>();

    @FXML
    protected void initialize() { }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        if (this.article != null) {
            this.toggleSelectButton.selectedProperty().unbindBidirectional(this.article.selectedProperty());
        }
        this.article = article;
        this.authorLabel.textProperty().bind(article.authorProperty());
        this.titleLabel.textProperty().bind(article.titleProperty());
        this.abstractLabel.textProperty().bind(article.abstractTextProperty());
        this.toggleSelectButton.setDisable(false);
        this.downloadButton.setDisable(!article.hasPDF());
        this.toggleSelectButton.selectedProperty().bindBidirectional(this.article.selectedProperty());
    }

    @FXML
    private void performDownload(ActionEvent event) {
        article.download();
    }

    @FXML
    private void performOpenSource(ActionEvent event) {
        if (article.getSourceURI() == null) {
            return;
        }
        MainController.openWebpage(article.getSourceURI());
    }

    @FXML
    private void performOpenCitations(ActionEvent event) {
        if (article.getCitationsURI() == null) {
            return;
        }
        MainController.openWebpage(article.getCitationsURI());    }

    @FXML
    private void performOpenRelated(ActionEvent event) {
        if (article.getRelatedURI() == null) {
            return;
        }
        MainController.openWebpage(article.getRelatedURI());    }
}
