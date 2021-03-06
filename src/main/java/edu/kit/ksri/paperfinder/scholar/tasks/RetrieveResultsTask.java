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
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by janscheurenbrand on 26.11.14.
 */
public class RetrieveResultsTask extends Task<ObservableList<Article>> {
    private static Logger logger = Logger.getLogger(RetrieveResultsTask.class.toString());

    private ReadOnlyObjectWrapper<ObservableList<Article>> partialResults = new ReadOnlyObjectWrapper<>(this, "partialResults", FXCollections.observableArrayList(new ArrayList()));
    private String q;
    private int n;
    private boolean includePatents;
    private boolean includeCitations;

    /**
     * Async Task to retrieve Articles from Google Scholar
     * Work with the ObservableList of Articles
     * @param q Search term
     * @param n Number of articles to fetch
     */
    public RetrieveResultsTask(String q, int n, boolean includePatents, boolean includeCitations) {
        this.q = q;
        this.n = n;
        this.includePatents = includePatents;
        this.includeCitations = includeCitations;
    }

    @Override
    protected ObservableList call() throws Exception {
        updateMessage("Retrieving Articles...");

        int resultsPerPage = Config.RESULTS_PER_PAGE;
        int numberOfPagesToFetch = (int) Math.ceil((double) n / (double) resultsPerPage);
        logger.info(String.format("Going to fetch %s pages", numberOfPagesToFetch));

        ContentRetriever contentRetriever = new ContentRetriever();
        int i = 0;
        String html = null;
        while (i < numberOfPagesToFetch) {
            logger.info("Fetching page " + (i+1));
            if (html == null) {
                html =  contentRetriever.getSingleResultPage(q, i, resultsPerPage, includePatents, includeCitations);
            }

            if (html.contains("captcha")) {
                updateMessage("FAILED TO LOAD RESULTS DUE TO UNUSUAL TRAFFIC. FURTHER ACTION IS REQUIRED.");

                SolveCaptchaTask solveCaptchaTask = new SolveCaptchaTask(html);
                Thread thread = new Thread(solveCaptchaTask);
                thread.start();
                thread.join();
                html = solveCaptchaTask.get();
            } else {
                ResultsParser resultsParser = new ResultsParser(html);
                List<Article> articleList = resultsParser.parse();
                partialResults.get().addAll(articleList);
                Comparator<Article> comparator = (a1, a2) -> Integer.compare(a2.getCitations(), a1.getCitations());
                FXCollections.sort(partialResults.get(), comparator);
                logger.info(String.format("Added %s articles from page %s", articleList.size(), (i+1)));
                updateMessage("Retrieved results: " + partialResults.get().size());
                html = null;

                // Check if there will be another page with results
                if (articleList.size() == resultsPerPage) {
                    int millis = new Random().nextInt(Config.SLEEP_WIGGLE) + Config.SLEEP_BASE;
                    logger.info(String.format("Sleeping for %s ms to prevent Google's anti-bot-ban", millis));
                    Thread.sleep(millis);
                    i++;
                } else {
                    i = numberOfPagesToFetch;
                }
            }

        }

        updateMessage("Retrieving finished. Retrieved results: " + partialResults.get().size());
        return partialResults.get();
    }

    public final ObservableList getPartialResults() {
        return partialResults.get();
    }
    public final ReadOnlyObjectProperty<ObservableList<Article>> partialResultsProperty() {
        return partialResults.getReadOnlyProperty();
    }

}