package edu.kit.ksri.paperfinder.scholar.tasks;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.scholar.http.ContentRetriever;
import edu.kit.ksri.paperfinder.scholar.parser.ResultsParser;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by janscheurenbrand on 26.11.14.
 */
public class RetrieveResultsTask extends Task<ObservableList<Article>> {

    private ReadOnlyObjectWrapper<ObservableList<Article>> partialResults = new ReadOnlyObjectWrapper<>(this, "partialResults", FXCollections.observableArrayList(new ArrayList()));
    private String q;
    private int n;

    /**
     * Async Task to retrieve Articles from Google Scholar
     * Work with the ObservableList of Articles
     * @param q Search term
     * @param n Number of Articles to fetch
     */
    public RetrieveResultsTask(String q, int n) {
        this.q = q;
        this.n = n;
    }

    @Override
    protected ObservableList call() throws Exception {
        updateMessage("Retrieving Articles...");

        int resultsPerPage = Config.RESULTS_PER_PAGE;
        int numberOfPagesToFetch = (int) Math.ceil((double) n / (double) resultsPerPage);

        ContentRetriever contentRetriever = new ContentRetriever();
        int i = 0;
        while (i <= numberOfPagesToFetch) {
            String html = contentRetriever.getSingleResultPage(q, i, resultsPerPage, false, false);
            ResultsParser resultsParser = new ResultsParser(html);
            List<Article> articleList = resultsParser.parse();
            partialResults.get().addAll(articleList);
            Comparator<Article> comparator = (a1, a2) -> Integer.compare(a2.getCitations(), a1.getCitations());
            FXCollections.sort(partialResults.get(), comparator);
            Thread.sleep(1000);
            i++;
        }

        updateMessage("Retrieved Articles");
        return partialResults.get();
    }

    public final ObservableList getPartialResults() {
        return partialResults.get();
    }
    public final ReadOnlyObjectProperty<ObservableList<Article>> partialResultsProperty() {
        return partialResults.getReadOnlyProperty();
    }

}