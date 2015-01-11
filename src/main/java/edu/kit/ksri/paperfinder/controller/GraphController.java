package edu.kit.ksri.paperfinder.controller;

import edu.kit.ksri.paperfinder.model.Article;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by janscheurenbrand on 19.12.14.
 */
public class GraphController {
    @FXML private AreaChart chart;
    @FXML private ComboBox comboBox;

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
            default:
        }
    }

    private void drawCitationsCumulativeGraph() {
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
    }

    private void drawCitationsGraph() {
        Comparator<Article> comparator = (a1, a2) -> Integer.compare(a2.getCitations(), a1.getCitations());
        List<Article> orderedArticleList = articleList.stream().sorted(comparator).collect(Collectors.toList());
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < orderedArticleList.size(); i++) {
            Article article = orderedArticleList.get(i);
            series.getData().add(new XYChart.Data(i, article.getCitations()));
        }

        chart.getData().clear();
        chart.getData().add(series);
    }

    private void drawPublishedGraph() {
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
                .forEach(entry -> series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue())));

        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setMinorTickVisible(false);

        NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        yAxis.setMinorTickVisible(false);
        yAxis.setTickUnit(1);

        chart.getData().clear();
        chart.getData().add(series);
    }
}
