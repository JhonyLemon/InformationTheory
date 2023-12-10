package pl.polsl.informationtheory.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.CompressionResult;
import pl.polsl.informationtheory.entity.FileInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Repository
public class CompressionRepository {
    private final Map<FileInfo, List<CompressionResult>> compressionResults = new HashMap<>();

    public void setCompressionResults(Map<FileInfo, List<CompressionResult>> results) {
        this.compressionResults.clear();
        this.compressionResults.putAll(results);
    }
}
