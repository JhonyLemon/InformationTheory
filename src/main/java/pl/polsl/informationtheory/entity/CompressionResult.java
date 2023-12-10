package pl.polsl.informationtheory.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CompressionResult {
    private final String algorithmClassName;
    private final Long initialSize;
    private final Long resultSize;
    private final double compressionRatio;

    public CompressionResult(String algorithmClassName, Long initialSize, Long resultSize) {
        this.algorithmClassName = algorithmClassName;
        this.initialSize = initialSize;
        this.resultSize = resultSize;
        this.compressionRatio = (double) initialSize / resultSize;
    }
}
