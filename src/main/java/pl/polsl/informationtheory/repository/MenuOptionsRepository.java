package pl.polsl.informationtheory.repository;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.Data;

import java.util.Comparator;

@Getter
@Repository
public class MenuOptionsRepository {
    @Accessors(fluent = true)
    private final BooleanProperty useUnicode = new SimpleBooleanProperty(false);
    private final ObjectProperty<Comparator<Data>> currentlySelectedComparator = new SimpleObjectProperty<>(Data.Comparator.countDescending());

}
