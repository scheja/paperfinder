package edu.kit.ksri.paperfinder.scholar;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.scholar.http.Request;
import edu.kit.ksri.paperfinder.scholar.http.Response;
import edu.kit.ksri.paperfinder.scholar.http.Verb;

/**
 * Created by janscheurenbrand on 27.11.14.
 */
public class ScholarAPI {

    /**
     * @param searchTerm The term you want to search for
     * @param i Index of the result page (= offset, if multiplied with 20), starting at 0!
     * @param includePatents Whether to include patents in the searach results
     * @param includeCitations Whether to include patents in the searach results
     * @return
     */
    public String getSingleResultPage(String searchTerm, int i, boolean includePatents, boolean includeCitations) {
        Request request = new Request(Verb.GET, Config.BASE_DOMAIN.concat("/scholar"));
        request.addURIParam("q",searchTerm);
        request.addURIParam("num", "20");
        if (i > 0) {
            request.addURIParam("start", String.valueOf(20*i));
        }
        request.addURIParam("as_sdt", (includePatents ? "0,5" : "1,5"));
        request.addURIParam("as_vis", (includeCitations ? "0" : "1"));

        Response response = request.execute();
        return response.getResponseBody();
    }



}
