package edu.kit.ksri.paperfinder.scholar.parser;

import edu.kit.ksri.paperfinder.scholar.util.FileUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ParserBaseTest {

    @Test
    public void testGetContent() throws Exception {
        String html = FileUtils.readFile(getClass().getResource("/results.html").getFile());
        ParserBase parser = new ResultsParser(html);
        assertTrue(parser.getContent().children().size() > 0);
    }
}