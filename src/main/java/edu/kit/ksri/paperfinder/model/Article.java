package edu.kit.ksri.paperfinder.model;

import javafx.beans.property.*;

/**
 * Created by janscheurenbrand on 26.11.14.
 */
public class Article {
    private StringProperty author = new SimpleStringProperty();
    private StringProperty title = new SimpleStringProperty();
    private StringProperty publication = new SimpleStringProperty();
    private StringProperty source = new SimpleStringProperty();
    private IntegerProperty yearPublished = new SimpleIntegerProperty();
    private IntegerProperty citations = new SimpleIntegerProperty();
    private StringProperty abstractText = new SimpleStringProperty();
    private StringProperty pdfLink = new SimpleStringProperty();
    private BooleanProperty selected = new SimpleBooleanProperty();
    private StringProperty sourceURI = new SimpleStringProperty();
    private StringProperty citationsURI = new SimpleStringProperty();
    private StringProperty relatedURI = new SimpleStringProperty();

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

    public String getPublication() {
        return publication.get();
    }

    public StringProperty publicationProperty() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication.set(publication);
    }

    public String getSource() {
        return source.get();
    }

    public StringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source.set(source);
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

    public String getAbstractText() {
        return abstractText.get();
    }

    public StringProperty abstractTextProperty() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText.set(abstractText);
    }

    public String getPdfLink() {
        return pdfLink.get();
    }

    public StringProperty pdfLinkProperty() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink.set(pdfLink);
    }

    public boolean hasPDF() {
        return pdfLink != null && pdfLink.get() != null && !pdfLink.get().isEmpty();
    }

    public boolean getSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public void toggleSelected() {
        this.setSelected(!this.getSelected());
    }

    public String getSourceURI() {
        return sourceURI.get();
    }

    public StringProperty sourceURIProperty() {
        return sourceURI;
    }

    public void setSourceURI(String sourceURI) {
        this.sourceURI.set(sourceURI);
    }

    public String getCitationsURI() {
        return citationsURI.get();
    }

    public StringProperty citationsURIProperty() {
        return citationsURI;
    }

    public void setCitationsURI(String citationsURI) {
        this.citationsURI.set(citationsURI);
    }

    public String getRelatedURI() {
        return relatedURI.get();
    }

    public StringProperty relatedURIProperty() {
        return relatedURI;
    }

    public void setRelatedURI(String relatedURI) {
        this.relatedURI.set(relatedURI);
    }

    @Override
    public String toString() {
        return author.get() + " -  " + title.get();
    }
}
