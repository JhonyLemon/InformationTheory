package pl.polsl.informationtheory.util;

public class MathExtension {

    private MathExtension(){}

    public static Double log(Double v, Integer base) {
        return Math.log(v) / Math.log(base);
    }

    public static Double round(Double v, Integer decimal) {
        Double decimalPoints = Math.pow(10, decimal);
        return Math.round(v * decimalPoints) / decimalPoints;
    }
}
