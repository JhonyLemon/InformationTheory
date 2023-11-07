package pl.polsl.informationtheory.util;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.enums.DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

public class StreamHelper {
    private StreamHelper(){}

    public static Predicate<Data> isType(DataType type) {
        return data -> type.equals(data.getType());
    }

    public static Predicate<Data> valueEquals(Data dataToMatch) {
        return data -> dataToMatch.getValue().equals(data.getValue());
    }

    public static DataCollector toSumedData(Integer count) {
        return new DataCollector(count);
    }

    @RequiredArgsConstructor
    private static final class DataCollector implements Collector<Data, List<Data>, List<Data>> {
    private final Integer count;

        @Override
        public Supplier<List<Data>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<Data>, Data> accumulator() {
            return (list, data) -> list.stream()
                    .filter(valueEquals(data))
                    .findFirst()
                    .ifPresentOrElse(
                            dataPresent -> dataPresent.addCount(data.getCount()),
                            () -> {
                                Data d = new Data(data.getType(), data.getValue(), data.getCount());
                                d.setCountAll(count);
                                list.add(d);
                            }
                    );
        }

        @Override
        public BinaryOperator<List<Data>> combiner() {
            return (left, right) -> {
                right.forEach(rData -> accumulator().accept(left, rData));
                return left;
            };
        }

        @Override
        public Function<List<Data>, List<Data>> finisher() {
            return f -> {
                f.forEach(Data::initialize);
                return f;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Sets.immutableEnumSet(Characteristics.UNORDERED);
        }

    }
}
