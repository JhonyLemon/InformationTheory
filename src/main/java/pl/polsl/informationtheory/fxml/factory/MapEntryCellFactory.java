package pl.polsl.informationtheory.fxml.factory;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;
import pl.polsl.informationtheory.enums.WhitespaceCharacters;

import java.util.Map;

@RequiredArgsConstructor
public class MapEntryCellFactory<V> implements Callback<ListView<Map.Entry<String, V>>, ListCell<Map.Entry<String, V>>> {

    private final BooleanProperty useUnicode;

    @Override
    public ListCell<Map.Entry<String, V>> call(ListView<Map.Entry<String, V>> param) {
        return new ListCell<>() {
            @Override
            public void updateItem(Map.Entry<String, V> entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setText(null);
                } else {
                    setText(escapeWhiteSpaceCharacters(entry.getKey(), useUnicode.get()) + ": " + entry.getValue());
                }
            }
        };
    }

    public String escapeWhiteSpaceCharacters(String key, boolean useUnicode) {
        char c = key.charAt(0);
        if (Character.isWhitespace(c)) {
            WhitespaceCharacters whitespaceCharacter = WhitespaceCharacters.findFromUniCode(c);
            return useUnicode ? whitespaceCharacter.getEscapedWhitespaceCharacter() : whitespaceCharacter.getLabel();
        }
        return key;
    }

}
