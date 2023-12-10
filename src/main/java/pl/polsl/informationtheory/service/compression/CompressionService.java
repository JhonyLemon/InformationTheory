package pl.polsl.informationtheory.service.compression;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.CompressionResult;
import pl.polsl.informationtheory.entity.FileCompressionSummary;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.repository.CompressionRepository;
import pl.polsl.informationtheory.service.compression.algorithm.report.ReportGenerator;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CompressionService {
    private final CompressionRepository compressionRepository;
    private final ReportGenerator reportGenerator;

    public List<FileCompressionSummary> getCompressionSummary() {
        Map<FileInfo, List<CompressionResult>> resultsMap = compressionRepository.getCompressionResults();
        return resultsMap.entrySet().stream().map(entry -> new FileCompressionSummary(entry.getKey(), entry.getValue())).toList();
    }

    public String getWinningFrequencyReport(List<FileCompressionSummary> data) {
        return reportGenerator.getWinningFrequencyReport(data);
    }

    public String getAverageCompressionRatioReport(List<FileCompressionSummary> data) {
        return reportGenerator.getAverageCompressionRatioReport(data);
    }
}
