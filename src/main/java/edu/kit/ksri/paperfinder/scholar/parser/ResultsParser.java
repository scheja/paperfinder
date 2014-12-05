package edu.kit.ksri.paperfinder.scholar.parser;

import edu.kit.ksri.paperfinder.Config;
import edu.kit.ksri.paperfinder.model.Article;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

import static edu.kit.ksri.paperfinder.util.NumberUtils.parseInt;


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
        try {
            article.setTitle(title.text());
        } catch (Exception e) {
            article.setTitle("n/a");
        }

        try {
            article.setSourceURI(title.attr("href"));
        } catch (Exception e) {
            article.setSourceURI("");
        }

        String[] metaInfoTextParts = new String[0];
        try {
            Element metaInfo = element.getElementsByClass("gs_a").first();
            String metaInfoText = metaInfo.text();
            metaInfoTextParts = metaInfoText.split(" - ");
            String author = metaInfoTextParts[0];
            article.setAuthor(author);
        } catch (Exception e) {
            article.setAuthor("n/a");
        }

        String publicationAndYear = null;
        try {
            publicationAndYear = metaInfoTextParts[1];
            String year = publicationAndYear.substring(publicationAndYear.length()-4, publicationAndYear.length());
            article.setYearPublished(parseInt(year, -1));
        } catch (Exception e) {
            article.setYearPublished(-1);
        }

        if (publicationAndYear.length() > 6) {
            try {
                String publication = publicationAndYear.substring(0, publicationAndYear.length()-4);
                article.setPublication(publication);
            } catch (Exception e) {
                article.setPublication("n/a");
            }
        }

        try {
            String source = metaInfoTextParts[metaInfoTextParts.length-1];
            article.setSource(source);
        } catch (Exception e) {
            article.setSource("n/a");
        }

        Element citationsElement = element.select(".gs_ri > .gs_fl > a").first();
        try {
            String citationText = citationsElement.text();
            article.setCitations(parseInt(citationText.replaceAll("[\\D]", ""),0));
        } catch (Exception e) {
            article.setCitations(-1);
        }

        try {
            article.setCitationsURI(Config.BASE_DOMAIN + citationsElement.attr("href"));
        } catch (Exception e) {
            article.setCitationsURI("");
        }

        try {
            Element relatedElement = citationsElement.nextElementSibling();

            article.setRelatedURI(Config.BASE_DOMAIN + relatedElement.attr("href"));
        } catch (Exception e) {
            article.setRelatedURI("");
        }

        try {
            Element abstractElement = element.select(".gs_ri > .gs_rs").first();
            String abstractText = abstractElement.text();
            article.setAbstractText(abstractText);
        } catch (Exception e) {
            article.setAbstractText("n/a");
        }

        try {
            Element pdfLinkElement = element.select(".gs_ttss > a").first();
            if (pdfLinkElement != null) {
                String pdfLink = pdfLinkElement.attr("href");
                article.setPdfLink(pdfLink);
            }
        } catch (Exception e) {
            article.setPdfLink("");
        }

        return article;
    }
}
