package edu.kit.ksri.paperfinder.scholar.parser;

import edu.kit.ksri.paperfinder.model.Article;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This parser works with the 2014-11-28 Google Scholar result HTML format
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

        Element title = element.select(".gs_rt > a").first();
        article.setTitle(title.text());

        Element metaInfo = element.getElementsByClass("gs_a").first();
        String metaInfoText = metaInfo.text();
        String[] metaInfoTextParts = metaInfoText.split(" - ");
        String author = metaInfoTextParts[0];
        article.setAuthor(author);

        String publicationAndYear = metaInfoTextParts[1];
        String year = publicationAndYear.substring(publicationAndYear.length()-4, publicationAndYear.length());
        article.setYearPublished(Integer.parseInt(year));

        if (publicationAndYear.length() > 6) {
            String publication = publicationAndYear.substring(0, publicationAndYear.length()-4);
            article.setPublication(publication);
        }

        String source = metaInfoTextParts[metaInfoTextParts.length-1];
        article.setSource(source);

        Element citationsElement = element.select(".gs_ri > .gs_fl > a").first();
        String citationText = citationsElement.text();
        article.setCitations(Integer.parseInt(citationText.replaceAll("[\\D]", "")));

        return article;
    }
}
