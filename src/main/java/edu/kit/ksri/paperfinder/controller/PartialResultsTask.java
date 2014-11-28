package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Paper;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Created by janscheurenbrand on 26.11.14.
 */
public class PartialResultsTask extends Task<ObservableList<Rectangle>> {
    private String searchString;

    public PartialResultsTask(String searchString) {
        this.searchString = searchString;
    }

    private ReadOnlyObjectWrapper<ObservableList> partialResults = new ReadOnlyObjectWrapper<>(this, "partialResults", FXCollections.observableArrayList(new ArrayList()));

    public final ObservableList getPartialResults() {
        return partialResults.get();
    }
    public final ReadOnlyObjectProperty<ObservableList> partialResultsProperty() {
        return partialResults.getReadOnlyProperty();
    }

    @Override
    protected ObservableList call() throws Exception {
        updateMessage("Retrieving Papers...");
        for (int i=1; i<=100; i++) {
            if (isCancelled()) break;
            final Paper p = new Paper();
            p.setAuthor("Author " + i);
            p.setTitle(searchString + " " + i);
            p.setCitations(i);

            final int ii = i;
            Platform.runLater(() -> {
                partialResults.get().add(p);
                updateMessage("Retrieving Papers (" + ii + "/" + 100 + ")");
            });
            Thread.sleep(30);
        }
        updateMessage("Retrieved Papers");
        return partialResults.get();
    }
}