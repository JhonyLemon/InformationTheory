package pl.polsl.informationtheory.fxml.factory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import pl.polsl.informationtheory.entity.CompressionResult;
import pl.polsl.informationtheory.entity.FileCompressionSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.polsl.informationtheory.service.compression.algorithm.util.CompressionRatioFormatter.getFormatted;

public class CompressionDataCellFactory implements Callback<ListView<FileCompressionSummary>, ListCell<FileCompressionSummary>> {

    @Override
    public ListCell<FileCompressionSummary> call(ListView<FileCompressionSummary> dataListView) {
        return new ListCell<>() {

            @Override
            protected void updateItem(FileCompressionSummary data, boolean b) {
                super.updateItem(data, b);
                if (Objects.isNull(data)) {
                    setText(null);
                    setGraphic(null);
                } else {
                    List<CompressionResult> compressionResults = data.getCompressionResults();

                    Long initialSize = compressionResults.stream().map(CompressionResult::getInitialSize).findFirst().orElse(null);
                    Label informationLabel = getInformationLabel(data.getInfo().getPath(), initialSize);

                    List<Label> labels = new ArrayList<>(List.of(informationLabel));
                    labels.addAll(getAlgorithmResultLabels(compressionResults));

                    HBox hBox = new HBox(labels.toArray(new Label[0]));
                    setGraphic(hBox);
                }
            }
        };
    }

    private Label getInformationLabel(String filePath, Long initialSize) {
        Label informationLabel = new Label();
        informationLabel.setText(
                String.format(
                        "File: %s with initial size: %s[B]",
                        filePath,
                        initialSize
                )
        );
        informationLabel.setStyle("-fx-font-weight: bold");
        return informationLabel;
    }

    private List<Label> getAlgorithmResultLabels(List<CompressionResult> compressionResults) {
        List<Label> labels = new ArrayList<>();
        for (CompressionResult result : compressionResults) {
            Label label = new Label();
            label.setPadding(new Insets(0, 10, 0, 10));
            label.setText(
                    String.format(
                            result.getAlgorithmClassName() + ": {result size: %s[B], CR: %s}",
                            result.getResultSize(),
                            getFormatted(result.getCompressionRatio())
                    )
            );
            labels.add(label);
        }
        return labels;
    }
}

