package edu.kit.ksri.paperfinder.scholar.util;

import edu.kit.ksri.paperfinder.model.Article;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ResultsParserTest {

    @Test
    public void testParse() throws Exception {
        String html = null;
        try {
            html = FileUtils.readFileToString(new File(getClass().getResource("/results.html").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultsParser parser = new ResultsParser(html);

        List<Article> articles = parser.parse();

        assertEquals(articles.size(), 20);
        assertEquals(articles.get(0).getTitle(), "Can quantum-mechanical description of physical reality be considered complete?");
    }
}