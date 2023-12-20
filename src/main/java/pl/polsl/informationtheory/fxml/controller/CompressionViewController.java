package pl.polsl.informationtheory.fxml.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.FileCompressionSummary;
import pl.polsl.informationtheory.fxml.factory.CompressionDataCellFactory;
import pl.polsl.informationtheory.service.compression.CompressionService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class CompressionViewController implements Initializable {

    private static final int ROW_HEIGHT = 25;

    @FXML
    private Label winningFrequency;
    @FXML
    private Label averageCompressionRatio;

    @FXML
    private ListView<FileCompressionSummary> compressionSummary;

    private final CompressionService compressionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        compressionSummary.setCellFactory(new CompressionDataCellFactory());
        cleanOldCompressionReportsIfNecessary();
    }

    public void defaultInit() {
        List<FileCompressionSummary> data = compressionService.getCompressionSummary();
        setCompressionSummary(data);
        compressionSummary.refresh();
        cleanOldCompressionReportsIfNecessary();
        addCompressionReportsToView(data);
    }

    private void setCompressionSummary(List<FileCompressionSummary> data) {
        compressionSummary.getItems().clear();
        compressionSummary.getItems().addAll(data);
    }

    private void cleanOldCompressionReportsIfNecessary() {
        winningFrequency.setText("");
        averageCompressionRatio.setText("");
    }

    private void addCompressionReportsToView(List<FileCompressionSummary> data) {
        String frequencyReport = compressionService.getWinningFrequencyReport(data);
        String averageCompressionRatioReport = compressionService.getAverageCompressionRatioReport(data);
        winningFrequency.setText(frequencyReport);
        averageCompressionRatio.setText(averageCompressionRatioReport);
    }
}
