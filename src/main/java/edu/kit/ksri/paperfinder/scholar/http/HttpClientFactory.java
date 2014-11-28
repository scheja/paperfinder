package edu.kit.ksri.paperfinder.scholar.http;

import edu.kit.ksri.paperfinder.scholar.http.cookies.CookieStoreFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.logging.Logger;

/**
 * Created by janscheurenbrand on 10.08.14.
 */

public class HttpClientFactory {
    static Logger logger = Logger.getLogger(HttpClientFactory.class.toString());

    static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36";

    static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setUserAgent(getUserAgent())
                .setDefaultCookieStore(CookieStoreFactory.getCookieStore())
                .build();
    }

    public static String getUserAgent() {
        return DEFAULT_USER_AGENT;
    }

}
