package edu.kit.ksri.paperfinder.scholar.http;

/**
 * Created by janscheurenbrand on 10.08.14.
 */
public class Methods {
    public static String ACCOUNT_OBTAIN_CSRF_TOKEN = "/qe/sync/";
    public static String ACCOUNT_LOGIN = "/accounts/login/";
    public static String ACCOUNT_CREATE = "/accounts/create/";
    public static String USER_INFO = "/users/%s/info/";
    public static String USER_FEED = "/feed/user/%s/?";
    public static String FRIENDSHIP_CREATE = "/friendships/create/%s/";
    public static String FRIENDSHIP_DESTROY = "/friendships/destroy/%s/";
    public static String FEED_POPULAR = "/feed/popular/?";
    public static String FEED_BY_TAG = "/feed/tag/%s/?";
}
