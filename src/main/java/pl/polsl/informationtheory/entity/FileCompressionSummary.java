package pl.polsl.informationtheory.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class FileCompressionSummary {
    private final FileInfo info;
    private final List<CompressionResult> compressionResults;
}
