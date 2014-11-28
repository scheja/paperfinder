package edu.kit.ksri.paperfinder.scholar.http;

import edu.kit.ksri.paperfinder.Config;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ContentRetrieverTest {

    @Test
    public void testGetSingleResultPage() throws Exception {
        ContentRetriever contentRetriever = new ContentRetriever();
        String result = contentRetriever.getSingleResultPage("Einstein", 0, Config.RESULTS_PER_PAGE, false, false);

        assertNotNull(result);
        assertTrue(result.contains("relativity"));
    }

}