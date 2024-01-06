package pl.polsl.informationtheory.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        this.compressionRatio = BigDecimal.valueOf((double) initialSize / resultSize).setScale(SpringContext.getBean(MenuOptionsRepository.class).getDecimalPlaces().get(), RoundingMode.HALF_UP).doubleValue();
    }
}
