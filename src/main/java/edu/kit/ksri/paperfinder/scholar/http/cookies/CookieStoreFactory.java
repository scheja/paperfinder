package edu.kit.ksri.paperfinder.scholar.http.cookies;

import java.util.logging.Logger;

/**
 * Created by janscheurenbrand on 11.08.14.
 */
public class CookieStoreFactory {
    static Logger logger = Logger.getLogger(CookieStoreFactory.class.toString());

    static CookieStore cookieStoreSingleton;

    public static CookieStore getCookieStore() {
        if (cookieStoreSingleton == null) {
            logger.info("Getting new CookieStore");
            cookieStoreSingleton = new FileCookieStore();
        }
        return cookieStoreSingleton;
    }
}
