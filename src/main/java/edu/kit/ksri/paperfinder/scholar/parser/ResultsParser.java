package edu.kit.ksri.paperfinder.scholar.parser;

import edu.kit.ksri.paperfinder.model.Article;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by janscheurenbrand on 28.11.14.
 */
public class ResultsParser extends ParserBase {
    public ResultsParser(String html) {
        super(html);
    }

    @Override
    public List<Article> parse() {
        return getContent().getElementsByClass("gs_r")
                .stream()
                .filter(element -> element.children().hasClass("gs_ri"))
                .map(this::buildArticleFromElement)
                .collect(Collectors.toList());
    }

    private Article buildArticleFromElement(Element element) {
        Article article = new Article();
        Element title = element.getElementsByClass("gs_rt").first();
        article.setTitle(title.text());
        Element author = element.getElementsByClass("gs_a").first();
        article.setAuthor(author.text());
        Element citationsElement = element.select(".gs_ri > .gs_fl > a").first();
        String citationText = citationsElement.text();
        article.setCitations(Integer.parseInt(citationText.replaceAll("[\\D]", "")));
        return article;
    }
}
