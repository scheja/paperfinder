package edu.kit.ksri.paperfinder.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by janscheurenbrand on 26.11.14.
 */
public class Article {
    private StringProperty author = new SimpleStringProperty();
    private StringProperty title = new SimpleStringProperty();
    private IntegerProperty yearPublished = new SimpleIntegerProperty();
    private IntegerProperty citations = new SimpleIntegerProperty();

    public Article() {}

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getYearPublished() {
        return yearPublished.get();
    }

    public IntegerProperty yearPublishedProperty() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished.set(yearPublished);
    }

    public int getCitations() {
        return citations.get();
    }

    public IntegerProperty citationsProperty() {
        return citations;
    }

    public void setCitations(int citations) {
        this.citations.set(citations);
    }
}
