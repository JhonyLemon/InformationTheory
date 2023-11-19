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

public class EntropyDataCellFactory implements Callback<ListView<Data>, ListCell<Data>> {
    private final BooleanProperty useUnicode = new SimpleBooleanProperty();

    public EntropyDataCellFactory(ObservableValue<Boolean> useUnicode) {
        this.useUnicode.bind(useUnicode);
    }

    @Override
    public ListCell<Data> call(ListView<Data> dataListView) {
        return new ListCell<>() {
            private final Label valueLabel = new Label("Value:");
            private final Label value = new Label();

            private final Label extraLabel1 = new Label("Information Amount:");
            private final Label extra1 = new Label();

            private final Label extraLabel2 = new Label("Entropy:");
            private final Label extra2 = new Label();
            private final HBox hBox = new HBox(valueLabel, value, extraLabel1, extra1, extraLabel2, extra2);


            @Override
            protected void updateItem(Data data, boolean b) {
                super.updateItem(data, b);
                if (Objects.isNull(data)) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                    value.setPadding(new Insets(0,10,0,10));
                    extra1.setPadding(new Insets(0,10,0,10));
                    extra2.setPadding(new Insets(0,10,0,10));
                    valueLabel.setStyle("-fx-font-weight: bold");
                    extraLabel1.setStyle("-fx-font-weight: bold");
                    extraLabel2.setStyle("-fx-font-weight: bold");

                    value.setText(escapeWhiteSpaceCharacters(data.getValue(), useUnicode.get()));
                    extra1.setText(
                            data.getElementaryInformationAmount().toString()
                    );
                    extra2.setText(
                            data.getEntropy().toString()
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
