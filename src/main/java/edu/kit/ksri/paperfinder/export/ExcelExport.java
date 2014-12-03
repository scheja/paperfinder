package edu.kit.ksri.paperfinder.export;

import edu.kit.ksri.paperfinder.model.Article;
import javafx.collections.ObservableList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Created by janscheurenbrand on 02.12.14.
 */
public class ExcelExport {

    private ObservableList<Article> articles;
    private File output;
    private Workbook wb;
    private Sheet sheet;
    private CreationHelper createHelper;

    public ExcelExport(ObservableList<Article> articles, File output) {
        this.articles = articles;
        this.output = output;
    }

    public void export() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("Export");
        createHelper = wb.getCreationHelper();

        // Iterate over articles with index
        IntStream.range(0, articles.size()).forEach(i -> addArticleToSheet(i, articles.get(i)));

        try {
            FileOutputStream fileOut = new FileOutputStream(output);
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addArticleToSheet(int index, Article article) {
        Row row = sheet.createRow(index);

        Cell cell = row.createCell(0);
        cell.setCellValue(createHelper.createRichTextString(article.getTitle()));

        cell = row.createCell(1);
        cell.setCellValue(createHelper.createRichTextString(article.getAuthor()));

        cell = row.createCell(2);
        cell.setCellValue(article.getYearPublished());

        cell = row.createCell(3);
        cell.setCellValue(article.getCitations());

        cell = row.createCell(4);
        cell.setCellValue(createHelper.createRichTextString(article.getPublication()));

        cell = row.createCell(5);
        cell.setCellValue(createHelper.createRichTextString(article.getSource()));

        cell = row.createCell(6);
        cell.setCellValue(createHelper.createRichTextString(article.getPdfLink()));

    }

}
