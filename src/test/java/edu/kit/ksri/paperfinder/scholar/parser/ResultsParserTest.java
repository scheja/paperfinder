package edu.kit.ksri.paperfinder.scholar.parser;

import edu.kit.ksri.paperfinder.model.Article;
import edu.kit.ksri.paperfinder.scholar.util.FileUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResultsParserTest {

    @Test
    public void testParse() throws Exception {
        String html = FileUtils.readFile(getClass().getResource("/results.html").getFile());
        ResultsParser parser = new ResultsParser(html);

        List<Article> articles = parser.parse();

        assertEquals(articles.size(), 20);
        assertEquals(articles.get(0).getTitle(), "Can quantum-mechanical description of physical reality be considered complete?");
    }
}