package edu.kit.ksri.paperfinder.scholar.tasks;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.scholar.http.Request;
import edu.kit.ksri.paperfinder.scholar.http.Response;
import edu.kit.ksri.paperfinder.scholar.http.Verb;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by janscheurenbrand on 02.12.14.
 */
public class SolveCaptchaTask extends Task<String> {

    private String html;
    private boolean goOn = false;
    private TextField textField;
    private Document document;
    private Stage dialogStage;

    public SolveCaptchaTask(String html) {
        this.html = html;
        this.document = Jsoup.parse(this.html);

    }

    @Override
    protected String call() throws Exception {

        Platform.runLater(this::showDialog);

        while(!goOn) {
            Thread.sleep(100);
        }

        return this.html;
    }

    private void showDialog() {
        dialogStage = new Stage();
        VBox root = new VBox();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(getImageHTML());
        textField = new TextField();
        Button button = new Button("Solve");
        button.setOnAction(this::solve);
        root.getChildren().addAll(webView, textField, button);
        Scene scene = new Scene(root, 300, 300);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private String getImageHTML() {
        Element imgTag = document.getElementsByTag("img").first();
        String src = Config.BASE_DOMAIN + imgTag.attr("src");
        imgTag.attr("src", src);
        return "<html><head><title>Captcha</title></head><body>"+imgTag.outerHtml()+"</body></html>";
    }

    private void solve(ActionEvent event) {
        String captchaText = textField.getText();
        String continueURL = document.select("input[name=continue]").first().val();
        String id = document.select("input[name=id]").first().val();


        Request request = new Request(Verb.GET, "http://ipv4.google.com/sorry/CaptchaRedirect");
        try {
            request.addURIParam("continue", URLEncoder.encode(continueURL, "UTF-8"));
        } catch (UnsupportedEncodingException e) {}
        request.addURIParam("id", id);
        request.addURIParam("captcha", captchaText);
        request.addURIParam("submit", "Submit");

        Response response = request.execute();
        this.html = response.getResponseBody();
        Platform.runLater(() -> this.dialogStage.close());
        this.goOn = true;
    }

}
