package edu.kit.ksri.paperfinder.model;

import java.util.Date;

/**
 * Created by janscheurenbrand on 26.11.14.
 */
public class Paper {
    private String author;
    private String title;
    private Date published;
    private int citations;

    public Paper() {}

    public Paper(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public int getCitations() {
        return citations;
    }

    public void setCitations(int citations) {
        this.citations = citations;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
