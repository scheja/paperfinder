package edu.kit.ksri.paperfinder.scholar;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScholarAPITest {

    @Test
    public void testGetSingleResultPage() throws Exception {
        ScholarAPI api = new ScholarAPI();
        String result = api.getSingleResultPage("Einstein", 0, false, false);

        assertNotNull(result);
        assertTrue(result.contains("relativity"));
    }
}