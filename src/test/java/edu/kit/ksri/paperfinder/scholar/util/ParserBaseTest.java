package edu.kit.ksri.paperfinder.scholar.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ParserBaseTest {

    @Test
    public void testGetContent() throws Exception {
        String html = null;
        try {
            html = FileUtils.readFileToString(new File(getClass().getResource("/results.html").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParserBase parser = new ResultsParser(html);
        assertTrue(parser.getContent().children().size() > 0);
    }
}