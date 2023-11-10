package pl.polsl.informationtheory.util;

import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.enums.DataType;

import java.util.function.*;

public class StreamHelper {
    private StreamHelper(){}

    public static Predicate<Data> isType(DataType type) {
        return data -> type.equals(data.getType());
    }

    public static Predicate<Data> valueEquals(Data dataToMatch) {
        return data -> dataToMatch.getValue().equals(data.getValue());
    }

}
