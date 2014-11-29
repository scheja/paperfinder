package edu.kit.ksri.paperfinder;

/**
 * Created by janscheurenbrand on 27.11.14.
 */
public class Config {
    public static final String CONFIG_PATH = System.getProperty("user.home") + "/paperfinder/";
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36";
    public static final String BASE_DOMAIN = "http://scholar.google.com/";
    public static final boolean TEST_MODE = false;
    public static final int RESULTS_PER_PAGE = 20;
    public static final int DEFAULT_NUMBER_OF_RESULTS = 20;
}
