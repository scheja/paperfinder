package edu.kit.ksri.paperfinder.scholar.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpClientFactoryTest {

    @Test
    public void testGetHttpClient() throws Exception {
        assertEquals(HttpClientFactory.getHttpClient(), HttpClientFactory.getHttpClient());
    }
}