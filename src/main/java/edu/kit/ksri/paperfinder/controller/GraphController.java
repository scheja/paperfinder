package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Article;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.GridPane;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by janscheurenbrand on 19.12.14.
 */
public class GraphController {
    @FXML private GridPane chartWrap;

    private ChartMode chartMode = ChartMode.CITATIONS;
    private List<Article> articleList;

    @FXML
    private void initialize() {

    }

    public void setChartMode(ChartMode chartMode) {
        this.chartMode = chartMode;
        drawGraph();
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
        drawGraph();
    }

    private void drawGraph() {
        chartWrap.getChildren().clear();
        switch (chartMode) {
            case CITATIONS:
                drawCitationsGraph();
                break;
            case CITATIONS_CUMULATIVE:
                drawCitationsCumulativeGraph();
                break;
            case PUBLISHED:
                drawPublishedGraph();
                break;
            case PUBLISHED_CUMULATIVE:
                drawPublishedCumulativeGraph();
                break;
            default:
        }
    }

    private void drawCitationsCumulativeGraph() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<Number,Number> chart = new AreaChart<>(xAxis,yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        Comparator<Article> comparator = (a1, a2) -> Integer.compare(a2.getCitations(), a1.getCitations());
        List<Article> orderedArticleList = articleList.stream().sorted(comparator).collect(Collectors.toList());
        XYChart.Series series = new XYChart.Series();

        int sum = 0;
        for (int i = 0; i < orderedArticleList.size(); i++) {
            Article article = orderedArticleList.get(i);
            sum = sum + article.getCitations();
            series.getData().add(new XYChart.Data(i, sum));
        }

        chart.getData().clear();
        chart.getData().add(series);
        chartWrap.add(chart,0,0);
    }

    private void drawCitationsGraph() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<Number,Number> chart = new AreaChart<>(xAxis,yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        Comparator<Article> comparator = (a1, a2) -> Integer.compare(a2.getCitations(), a1.getCitations());
        List<Article> orderedArticleList = articleList.stream().sorted(comparator).collect(Collectors.toList());
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < orderedArticleList.size(); i++) {
            Article article = orderedArticleList.get(i);
            series.getData().add(new XYChart.Data(i, article.getCitations()));
        }

        chart.getData().clear();
        chart.getData().add(series);
        chartWrap.add(chart, 0, 0);
    }

    private void drawPublishedGraph() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        yAxis.setMinorTickVisible(false);
        BarChart<String,Number> chart = new BarChart<>(xAxis,yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        if (articleList.size() > 0) {
            HashMap<Integer,Integer> distribution = new HashMap<>();

            articleList.stream().forEach(article -> {
                distribution.put(article.getYearPublished(), distribution.getOrDefault(article.getYearPublished(),0)+1);
            });

            int min = distribution.keySet().stream().min(Comparator.<Integer>naturalOrder()).get();
            int max = distribution.keySet().stream().max(Comparator.<Integer>naturalOrder()).get();

            for (int i = min-1; i <= max+1; i++) {
                distribution.putIfAbsent(i,0);
            }

            XYChart.Series series = new XYChart.Series();

            distribution.entrySet().stream()
                    .forEach(entry -> series.getData().add(new XYChart.Data(String.valueOf(entry.getKey()), entry.getValue())));

            chart.getData().clear();
            chart.getData().add(series);
            chartWrap.add(chart,0,0);
        }
    }

    private void drawPublishedCumulativeGraph() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setMinorTickVisible(false);
        yAxis.setTickUnit(1);
        AreaChart<String,Number> chart = new AreaChart<>(xAxis,yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        HashMap<Integer,Integer> distribution = new HashMap<>();

        articleList.stream().forEach(article -> {
            distribution.put(article.getYearPublished(), distribution.getOrDefault(article.getYearPublished(),0)+1);
        });

        int min = distribution.keySet().stream().min(Comparator.<Integer>naturalOrder()).get();
        int max = distribution.keySet().stream().max(Comparator.<Integer>naturalOrder()).get();

        XYChart.Series series = new XYChart.Series();

        int sum = 0;
        for (int i = min; i <= max; i++) {
            int thisYearsPublications = distribution.getOrDefault(i, 0);
            sum = sum + thisYearsPublications;
            series.getData().add(new XYChart.Data(String.valueOf(i), sum));
        }


        chart.getData().clear();
        chart.getData().add(series);
        chartWrap.add(chart,0,0);
    }

    public enum ChartMode {
        CITATIONS, CITATIONS_CUMULATIVE, PUBLISHED, PUBLISHED_CUMULATIVE
    }
}
