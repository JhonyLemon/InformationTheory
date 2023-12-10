package pl.polsl.informationtheory.service.compression.algorithm.util;

import java.util.Map;

import static pl.polsl.informationtheory.service.compression.algorithm.util.CompressionRatioFormatter.getFormatted;

public class DisplayHelper {

    public static String displayFrequency(Map<String, Integer> algorithmMap) {
        int filesCount = algorithmMap.values().stream().mapToInt(Integer::intValue).sum();
        StringBuilder sb = new StringBuilder();
        algorithmMap.forEach((key, value) -> sb.append(key).append(": ").append(value).append("/").append(filesCount).append("  "));
        return sb.toString();
    }

    public static String displayAverage(Map<String, Double> algorithmMap) {
        StringBuilder sb = new StringBuilder();
        algorithmMap.forEach((key, value) -> sb.append(key).append(": ").append(getFormatted(value)).append("  "));
        return sb.toString();
    }
}
