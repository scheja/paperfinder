package edu.kit.ksri.paperfinder.scholar.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by janscheurenbrand on 10.08.14.
 */
public class Response {
    static Logger logger = Logger.getLogger(Response.class.toString());
    private int statusCode;
    private String responseBody;

    public Response(Request request) {
        logger.info(String.format("Initializing Response"));
        HttpRequestBase requestBase = request.getRequest();
        CloseableHttpClient client = HttpClientFactory.getHttpClient();

        try {
            logger.info("Executing Response");
            CloseableHttpResponse response = client.execute(requestBase);

            try {
                this.statusCode = response.getStatusLine().getStatusCode();
                logger.info(String.format("Executed Response. Status Code %s", this.statusCode));
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                this.responseBody = result.toString();
                logger.info("Response Content:");
                logger.info(this.responseBody);
            } finally {
                response.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponseBody() {
        return responseBody;
    }

    public boolean wasSuccessful() {
        return (this.statusCode >= 200) && (this.statusCode < 400);
    }
}
