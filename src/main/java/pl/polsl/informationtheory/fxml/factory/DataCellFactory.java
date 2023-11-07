package pl.polsl.informationtheory.fxml.factory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.enums.WhitespaceCharacters;

import java.math.BigDecimal;
import java.util.Objects;

public class DataCellFactory implements Callback<ListView<Data>, ListCell<Data>> {
    private final BooleanProperty useUnicode = new SimpleBooleanProperty();

    public DataCellFactory(BooleanProperty property) {
        this.useUnicode.bind(property);
    }

    public DataCellFactory(ObservableValue<? extends Boolean> useUnicode) {
        this.useUnicode.bind(useUnicode);
    }

    @Override
    public ListCell<Data> call(ListView<Data> dataListView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Data data, boolean b) {
                super.updateItem(data, b);
                if (Objects.isNull(data)) {
                    setText("");
                } else {
                    setText(
                            String.format("%s: %s%% (%s/%s)",
                                    escapeWhiteSpaceCharacters(data.getValue(), useUnicode.get()),
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
