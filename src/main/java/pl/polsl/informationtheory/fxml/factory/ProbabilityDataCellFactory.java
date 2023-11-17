package pl.polsl.informationtheory.fxml.factory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.enums.WhitespaceCharacters;

import java.math.BigDecimal;
import java.util.Objects;

public class ProbabilityDataCellFactory implements Callback<ListView<Data>, ListCell<Data>> {
    private final BooleanProperty useUnicode = new SimpleBooleanProperty();

    public ProbabilityDataCellFactory(ObservableValue<Boolean> useUnicode) {
        this.useUnicode.bind(useUnicode);
    }

    @Override
    public ListCell<Data> call(ListView<Data> dataListView) {
        return new ListCell<>() {
            private final Label valueLabel = new Label("Value:");
            private final Label value = new Label();

            private final Label extraLabel = new Label("Probability:");
            private final Label extra = new Label();
            private final HBox hBox = new HBox(valueLabel, value, extraLabel, extra);


            @Override
            protected void updateItem(Data data, boolean b) {
                super.updateItem(data, b);
                if (Objects.isNull(data)) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                    value.setPadding(new Insets(0,10,0,10));
                    extra.setPadding(new Insets(0,10,0,10));
                    valueLabel.setStyle("-fx-font-weight: bold");
                    extraLabel.setStyle("-fx-font-weight: bold");

                    value.setText(escapeWhiteSpaceCharacters(data.getValue(), useUnicode.get()));
                    extra.setText(
                            String.format(
                                    "%s%% (%s/%s)",
                                    data.getProbability().multiply(BigDecimal.valueOf(100)),
                                    data.getCount(),
                                    data.getCountAll()
                            )
                    );
                }
            }
        };
    }


    private String escapeWhiteSpaceCharacters(String key, boolean useUnicode) {
        char c = key.charAt(0);
        if (Character.isWhitespace(c)) {
            WhitespaceCharacters whitespaceCharacter = WhitespaceCharacters.findFromUniCode(c);
            return useUnicode ? whitespaceCharacter.getEscapedWhitespaceCharacter() : whitespaceCharacter.getLabel();
        }
        return key;
    }


}
