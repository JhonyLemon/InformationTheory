package pl.polsl.informationtheory.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.enums.DataType;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;
import pl.polsl.informationtheory.util.MathExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Comparator;

import static pl.polsl.informationtheory.util.MathExtension.log;

@Getter
@EqualsAndHashCode
public class Data {
    private final DataType type;
    private final String value;
    private Integer count = 0;
    private Integer countAll = 0;
    private BigDecimal probability = BigDecimal.ZERO;
    private BigDecimal elementaryInformationAmount = BigDecimal.ZERO;
    private BigDecimal entropy = BigDecimal.ZERO;
    @Getter(AccessLevel.NONE)
    private boolean initialized = false;

    public Data(DataType type, String value, Integer count) {
        this.type = type;
        this.value = value;
        this.count = count;
    }

    public void initialize() {
        if(!initialized) {
            MenuOptionsRepository mor = SpringContext.getBean(MenuOptionsRepository.class);
            this.initialized = true;
            this.probability = BigDecimal.valueOf(this.count)
                      .setScale(100, RoundingMode.HALF_UP)
                      .divide(BigDecimal.valueOf(this.countAll), RoundingMode.HALF_UP);
            this.elementaryInformationAmount = BigDecimal.valueOf(MathExtension.log(this.probability.doubleValue(), mor.getLogarithmBase().get())).negate();
            this.entropy = this.probability.multiply(this.elementaryInformationAmount);

            this.probability = this.probability.setScale(mor.getDecimalPlaces().get(), RoundingMode.HALF_UP);
            this.entropy = this.entropy.setScale(mor.getDecimalPlaces().get(), RoundingMode.HALF_UP);
        }
    }

    public void addCount(Integer value) {
        if (!initialized) {
            this.count+=value;
        }
    }

    public void setCountAll(Integer countAll) {
        if (!initialized) {
            this.countAll = countAll;
        }
    }

    public static final class Comparator {
        private Comparator(){}
        private static final java.util.Comparator<Data> valueComparator = java.util.Comparator.comparing(Data::getValue);
        private static final java.util.Comparator<Data> countComparator = java.util.Comparator.comparing(Data::getCount);

        public static java.util.Comparator<Data> valueAscending() {
            return valueComparator;
        }

        public static java.util.Comparator<Data> valueDescending() {
            return valueComparator.reversed();
        }

        public static java.util.Comparator<Data> countAscending() {
            return countComparator;
        }

        public static java.util.Comparator<Data> countDescending() {
            return countComparator.reversed();
        }
    }

}
