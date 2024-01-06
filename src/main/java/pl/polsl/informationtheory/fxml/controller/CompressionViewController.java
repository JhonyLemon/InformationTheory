package pl.polsl.informationtheory.fxml.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.FileCompressionSummary;
import pl.polsl.informationtheory.fxml.factory.CompressionDataCellFactory;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;
import pl.polsl.informationtheory.service.compression.CompressionService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER;
import static pl.polsl.informationtheory.service.compression.algorithm.util.DisplayHelper.displayAverage;
import static pl.polsl.informationtheory.service.compression.algorithm.util.DisplayHelper.displayFrequency;

@Component
@RequiredArgsConstructor
public class CompressionViewController implements Initializable {

    @FXML
    private Label winningFrequency;
    @FXML
    private Label averageCompressionRatio;

    private final MenuOptionsRepository menuOptionsRepository;

    @FXML
    private ListView<FileCompressionSummary> compressionSummary;

    private final CompressionService compressionService;
    private Map<String, Integer> frequencyMap = new HashMap<>();
    private Map<String, Double> compressionRatioMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        compressionSummary.setCellFactory(new CompressionDataCellFactory());
        setCompressionSummaryListViewClickListener();
        setWinningFrequencyLabelClickListener();
        setAverageCompressionRatioLabelClickListener();
        cleanOldCompressionReportsIfNecessary();
    }

    public void defaultInit() {
        List<FileCompressionSummary> data = compressionService.getCompressionSummary();
        setCompressionSummary(data);
        compressionSummary.refresh();
        cleanOldCompressionReportsIfNecessary();
        initCompressionData(data);
        setCompressionReports();
    }

    private void setCompressionSummary(List<FileCompressionSummary> data) {
        compressionSummary.getItems().clear();
        compressionSummary.getItems().addAll(data);
    }

    private void cleanOldCompressionReportsIfNecessary() {
        frequencyMap.clear();
        compressionRatioMap.clear();
        winningFrequency.setText("");
        averageCompressionRatio.setText("");
    }

    private void initCompressionData(List<FileCompressionSummary> data) {
        frequencyMap = compressionService.getWinningFrequencyReport(data);
        compressionRatioMap = compressionService.getAverageCompressionRatioReport(data);
    }

    private void setCompressionReports() {
        String frequencyReport = displayFrequency(frequencyMap);
        String averageCompressionRatioReport = displayAverage(compressionRatioMap);
        winningFrequency.setText(frequencyReport);
        averageCompressionRatio.setText(averageCompressionRatioReport);
    }

    private void setCompressionSummaryListViewClickListener() {
        compressionSummary.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && compressionSummary.getSelectionModel().getSelectedItem() != null) {
                showCompressionSummaryChartWindow(compressionSummary.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void setWinningFrequencyLabelClickListener() {
        winningFrequency.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showWinningFrequencyChartWindow();
            }
        });
    }

    private void setAverageCompressionRatioLabelClickListener() {
        averageCompressionRatio.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showAverageCompressionRatioChartWindow();
            }
        });
    }

    private void showCompressionSummaryChartWindow(FileCompressionSummary selectedSummary) {
        Stage chartStage = new Stage();
        String fileName = selectedSummary.getInfo().getPath();
        chartStage.setTitle("Compression summary for file: " + fileName);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Compression algorithm");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Compression ratio");
        BarChart<String, Number> compressionRatioBarChart = new BarChart<>(xAxis, yAxis);
        compressionRatioBarChart.setTitle("Compression ratio chart");

        XYChart.Series<String, Number> winningFrequencyData = getCompressionRatioSeriesData(selectedSummary);
        compressionRatioBarChart.getData().add(winningFrequencyData);

        chartStage.setScene(new Scene(compressionRatioBarChart, 800, 600));
        chartStage.show();
    }

    private void showWinningFrequencyChartWindow() {
        Stage chartStage = new Stage();
        chartStage.setTitle("Compression winning frequency chart");

        ObservableList<PieChart.Data> pieChartData = getFrequencyPieChartData();
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Winning frequency chart");

        Scene chartScene = new Scene(pieChart, 800, 600);
        chartStage.setScene(chartScene);
        chartStage.show();
    }

    private void showAverageCompressionRatioChartWindow() {
        Stage chartStage = new Stage();
        chartStage.setTitle("Average compression ratio chart");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Compression algorithm");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Average compression ratio");
        BarChart<String, Number> compressionRatioBarChart = new BarChart<>(xAxis, yAxis);
        compressionRatioBarChart.setTitle("Compression ratio chart");

        XYChart.Series<String, Number> compressionRatioData = getAverageCompressionRatioSeriesData();
        compressionRatioBarChart.getData().add(compressionRatioData);

        chartStage.setScene(new Scene(compressionRatioBarChart, 800, 600));
        chartStage.show();
    }

    private XYChart.Series<String, Number> getCompressionRatioSeriesData(FileCompressionSummary fileCompressionSummary) {
        XYChart.Series<String, Number> winningFrequencySeriesData = new XYChart.Series<>();

        fileCompressionSummary.getCompressionResults().forEach(element ->
        {
            winningFrequencySeriesData.getData().add(createData(element.getAlgorithmClassName(), element.getCompressionRatio()));
        });

        return winningFrequencySeriesData;
    }

    private XYChart.Series<String, Number> getAverageCompressionRatioSeriesData() {
        XYChart.Series<String, Number> compressionRatioSeriesData = new XYChart.Series<>();

        compressionRatioMap.forEach((key, value) ->
                compressionRatioSeriesData.getData().add(createData(key, value)));

        return compressionRatioSeriesData;
    }

    private ObservableList<PieChart.Data> getFrequencyPieChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int count = compressionSummary.getItems().size();

        frequencyMap.forEach((key, value) -> {
            double calculatedValue = BigDecimal.valueOf(100 *(double) value / count).setScale(menuOptionsRepository.getDecimalPlaces().get(), RoundingMode.HALF_UP).doubleValue();

            PieChart.Data data = new PieChart.Data(key, calculatedValue);
            String percentage =  calculatedValue + "%";
            data.nameProperty().set(percentage);
            data.setName(key + "[" + percentage + "]");
            pieChartData.add(data);
        });

        return pieChartData;
    }

    private XYChart.Data<String, Number> createData(String key, double value) {
        XYChart.Data<String, Number> data = new XYChart.Data<>(key, value);

        StackPane node = new StackPane();
        Label label = new Label(String.format("%.2f", BigDecimal.valueOf(value).setScale(menuOptionsRepository.getDecimalPlaces().get(), RoundingMode.HALF_UP).doubleValue()));
        Group group = new Group(label);
        StackPane.setAlignment(group, CENTER);
        node.getChildren().add(group);
        data.setNode(node);

        return data;
    }
}
