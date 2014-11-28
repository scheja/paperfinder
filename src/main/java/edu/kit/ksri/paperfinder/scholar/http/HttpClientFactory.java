package edu.kit.ksri.paperfinder.scholar.http;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.scholar.http.cookies.CookieStoreFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.logging.Logger;

/**
 * Created by janscheurenbrand on 10.08.14.
 */

public class HttpClientFactory {
    private static Logger logger = Logger.getLogger(HttpClientFactory.class.toString());
    private static CloseableHttpClient httpClientSingleton;

    public static CloseableHttpClient getHttpClient() {
        if (httpClientSingleton == null) {
            logger.info("Building new HTTPClient");
            httpClientSingleton = HttpClients.custom()
                    .setUserAgent(getUserAgent())
                    .setDefaultCookieStore(CookieStoreFactory.getCookieStore())
                    .build();
        }
        return httpClientSingleton;
    }

    public static String getUserAgent() {
        return Config.USER_AGENT;
    }

}
