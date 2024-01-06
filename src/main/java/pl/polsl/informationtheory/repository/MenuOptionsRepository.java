package pl.polsl.informationtheory.repository;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.Data;

import java.util.Comparator;

@Getter
@Repository
public class MenuOptionsRepository {
    @Accessors(fluent = true)
    private final BooleanProperty useUnicode = new SimpleBooleanProperty(true);
    private final ObjectProperty<Comparator<Data>> currentlySelectedComparator = new SimpleObjectProperty<>(Data.Comparator.countDescending());
    private final IntegerProperty logarithmBase = new SimpleIntegerProperty(2);
    private final IntegerProperty decimalPlaces = new SimpleIntegerProperty(10);

    public void setLogarithmBase(String value) {
        logarithmBase.setValue(Integer.valueOf(value));
    }

    public void setDecimalPlaces(String value) {
        decimalPlaces.setValue(Integer.valueOf(value));
    }
}
