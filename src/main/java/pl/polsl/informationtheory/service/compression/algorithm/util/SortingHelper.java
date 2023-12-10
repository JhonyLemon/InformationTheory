package pl.polsl.informationtheory.service.compression.algorithm.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SortingHelper {

    public static <T extends Comparable<T>> Map<String, T> sortByValueDescending(Map<String, T> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, T>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
