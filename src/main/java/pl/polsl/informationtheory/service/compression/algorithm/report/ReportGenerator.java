package pl.polsl.informationtheory.service.compression.algorithm.report;

import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.CompressionResult;
import pl.polsl.informationtheory.entity.FileCompressionSummary;

import java.util.*;
import java.util.stream.Collectors;

import static pl.polsl.informationtheory.service.compression.algorithm.util.SortingHelper.sortByValueDescending;

@Component
public class ReportGenerator {

    public Map<String, Integer> getWinningFrequencyReport(List<FileCompressionSummary> data) {
        Map<String, Integer> winningFrequencyMap = calculateWinningFrequency(data);
        return sortByValueDescending(winningFrequencyMap);
    }

    public Map<String, Double> getAverageCompressionRatioReport(List<FileCompressionSummary> data) {
        Map<String, Double> averageCompressionRatios = calculateAverageCompressionRatios(data);
        return sortByValueDescending(averageCompressionRatios);
    }

    private Map<String, Integer> calculateWinningFrequency(List<FileCompressionSummary> data) {
        Map<String, Integer> frequencyMap = initFrequencyMap(data);
        data.forEach(x -> {
                    CompressionResult result = x.getCompressionResults().stream().max(Comparator.comparingDouble(CompressionResult::getCompressionRatio)).orElse(null);
                    if (Objects.nonNull(result)) {
                        String key = result.getAlgorithmClassName();
                        Integer currentCount = frequencyMap.get(key);
                        frequencyMap.put(key, currentCount + 1);
                    }
                }
        );
        return frequencyMap;
    }

    private Map<String, Integer> initFrequencyMap(List<FileCompressionSummary> data) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String algorithmName : getAlgorithmNames(data)) {
            frequencyMap.put(algorithmName, 0);
        }
        return frequencyMap;
    }

    private Set<String> getAlgorithmNames(List<FileCompressionSummary> data) {
        return data.stream()
                .map(FileCompressionSummary::getCompressionResults)
                .flatMap(Collection::stream)
                .map(CompressionResult::getAlgorithmClassName)
                .collect(Collectors.toSet());
    }

    private Map<String, Double> calculateAverageCompressionRatios(List<FileCompressionSummary> data) {
        return data.stream()
                .flatMap(summary -> summary.getCompressionResults().stream())
                .collect(Collectors.groupingBy(
                        CompressionResult::getAlgorithmClassName,
                        Collectors.averagingDouble(CompressionResult::getCompressionRatio)
                ));
    }
}
