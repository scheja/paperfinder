package edu.kit.ksri.paperfinder.scholar.http;

import edu.kit.ksri.paperfinder.scholar.http.cookies.CookieStoreFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by janscheurenbrand on 10.08.14.
 */
public class Request {
    private static Logger logger = Logger.getLogger(Request.class.toString());
    private Verb verb = Verb.GET;
    private String uri = null;
    private Map<String, String> uriParams = new HashMap<>();
    private Map<String, String> bodyParams = new HashMap<>();
    private HttpRequestBase request;

    public Request(Verb verb, String uri) {
        this.verb = verb;
        this.uri = uri;
    }

    public Response execute() {
        this.uri = addUriParams(uri);

        logger.info("Creating HTTPClient Request Object");
        switch (verb) {
            case GET:
                request = new HttpGet(this.uri);
                break;
            case PUT:
                request = new HttpPut(this.uri);
                break;
            case POST:
                request = new HttpPost(this.uri);
                break;
            case DELETE:
                request = new HttpDelete(this.uri);
                break;
        }

        request.setHeader("User-Agent", HttpClientFactory.getUserAgent());

        if (verb.equals(Verb.PUT) || verb.equals(Verb.POST)) {
            addBodyParams(request);
        }

        return new Response(this);
    }

    private String addUriParams(String uri) {
        logger.info("Adding URI Params");
        if (uriParams.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : uriParams.entrySet()) {
                if (i == 0) {
                    uri = uri + "?";
                } else {
                    uri = uri + "&";
                }
                uri = uri + String.format("%s=%s", entry.getKey(), entry.getValue());
                i++;
            }
        }
        return uri;
    }


    private void addBodyParams(HttpRequestBase request)  {
        logger.info("Adding Body Params");

        CookieStoreFactory.getCookieStore().getCookies()
                .stream()
                .filter(cookie -> cookie.getName().equals("csrftoken"))
                .map(cookie -> this.bodyParams.put("_csrftoken", cookie.getValue()));

        List<NameValuePair> nameValuePairs = bodyParams.entrySet()
                .stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        try {
            ((HttpEntityEnclosingRequestBase) request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            logger.severe("Failed to add body params due to encoding exception");
            e.printStackTrace();
        }
    }

    public void addURIParam(String name, String value) {
        uriParams.put(name, value);
    }

    public void addBodyParam(String name, String value) {
        bodyParams.put(name, value);
    }

    public HttpRequestBase getRequest() {
        return request;
    }

    public void setRequest(HttpRequestBase request) {
        this.request = request;
    }
}
