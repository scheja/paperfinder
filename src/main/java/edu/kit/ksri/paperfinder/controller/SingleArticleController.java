package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Article;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    @FXML private Label status;
    private ObservableList<Article> results;
    private List<Article> completeResultList = new ArrayList<>();

    @FXML
    protected void initialize() { }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        this.authorLabel.textProperty().bind(article.authorProperty());
        this.titleLabel.textProperty().bind(article.titleProperty());
        this.abstractLabel.textProperty().bind(article.abstractTextProperty());

    }
}
