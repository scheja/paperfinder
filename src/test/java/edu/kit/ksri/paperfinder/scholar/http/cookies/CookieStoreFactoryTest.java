package edu.kit.ksri.paperfinder.scholar.http.cookies;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CookieStoreFactoryTest {

    @Test
    public void testGetCookieStore() throws Exception {
        assertEquals(CookieStoreFactory.getCookieStore(),CookieStoreFactory.getCookieStore());
    }
}