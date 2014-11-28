package edu.kit.ksri.paperfinder.scholar.http.cookies;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileCookieStoreTest {

    FileCookieStore fileCookieStore;

    @Before
    public void setUp() throws Exception {
        fileCookieStore = new FileCookieStore("test");
    }

    @Test
    public void testAddCookie() throws Exception {
        Cookie cookie = new BasicClientCookie("foo", "bar");
        fileCookieStore.addCookie(cookie);

        assertEquals(fileCookieStore.getCookies().size(), 1);

        fileCookieStore.clear();
    }

    @Test
    public void testGetCookies() throws Exception {
        Cookie cookie = new BasicClientCookie("foo", "bar");
        fileCookieStore.addCookie(cookie);
        assertEquals(fileCookieStore.getCookies().get(0).getValue(), "bar");
    }

    @Test
    public void testClear() throws Exception {
        Cookie cookie = new BasicClientCookie("foo", "bar");
        fileCookieStore.addCookie(cookie);
        fileCookieStore.clear();
        assertEquals(fileCookieStore.getCookies().size(), 0);
    }
}