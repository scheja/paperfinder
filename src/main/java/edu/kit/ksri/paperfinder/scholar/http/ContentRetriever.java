package edu.kit.ksri.paperfinder.scholar.http;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.scholar.util.FileUtils;

/**
 * Created by janscheurenbrand on 28.11.14.
 */
public class ContentRetriever {

    /**
     * @param searchTerm The term you want to search for
     * @param i Index of the result page (= offset, if multiplied with n), starting at 0!
     * @param n Number of results per page. MUST be between 10 and 20
     * @param includePatents Whether to include patents in the searach results
     * @param includeCitations Whether to include patents in the searach results
     * @return
     */
    public String getSingleResultPage(String searchTerm, int i, int n, boolean includePatents, boolean includeCitations) {

        // To reduce real requests to Google while testing
        if (Config.TEST_MODE) {
            return FileUtils.readFile(getClass().getResource("/results.html").getFile());
        }

        Request request = new Request(Verb.GET, Config.BASE_DOMAIN.concat("/scholar"));
        request.addURIParam("q",searchTerm);
        request.addURIParam("num", String.valueOf(n));
        if (i > 0) {
            request.addURIParam("start", String.valueOf(n*i));
        }
        request.addURIParam("as_sdt", (includePatents ? "0,5" : "1,5"));
        request.addURIParam("as_vis", (includeCitations ? "0" : "1"));

        Response response = request.execute();
        return response.getResponseBody();
    }

}
