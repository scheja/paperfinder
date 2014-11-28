package edu.kit.ksri.paperfinder.scholar.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by janscheurenbrand on 28.11.14.
 */
public abstract class ParserBase {

    private String html;
    private Document document;
    private Element content;

    public ParserBase(String html) {
        this.html = html;
        this.document = Jsoup.parse(this.html);
        this.content = document.getElementById("gs_ccl");
    }

    public Element getContent() {
        return content;
    }

    public abstract List<?> parse();
}
