package pl.polsl.informationtheory.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathExtension {

    private MathExtension(){}

    public static Double log2(Double v, Integer base) {
        return Math.log(v) / Math.log(base);
    }

    public static Double round(Double v, Integer decimal) {
        Double decimalPoints = Math.pow(10, decimal);
        return Math.round(v * decimalPoints) / decimalPoints;
    }

    //NOT WORKING
    /**
     * from www.java2s.com
     * @see <a href="http://stackoverflow.com/questions/739532/logarithm-of-a-bigdecimal">for</a>
     * reference.
     * @param base_int baseOfLog
     * @param x value
     * @return returnValue
     */
    public static BigDecimal log(int base_int, BigDecimal x) {
        BigDecimal result = BigDecimal.ZERO;

        BigDecimal input = new BigDecimal(x.toString());
        int decimalPlaces = 100;
        int scale = input.precision() + decimalPlaces;

        int maxite = 10000;
        int ite = 0;
        BigDecimal maxError_BigDecimal = new BigDecimal(BigInteger.ONE, decimalPlaces + 1);
        // System.out.println("maxError_BigDecimal " + maxError_BigDecimal);
        // System.out.println("scale " + scale);

        RoundingMode a_RoundingMode = RoundingMode.UP;

        BigDecimal two_BigDecimal = new BigDecimal("2");
        BigDecimal base_BigDecimal = new BigDecimal(base_int);

        while (input.compareTo(base_BigDecimal) == 1) {
            result = result.add(BigDecimal.ONE);
            input = input.divide(base_BigDecimal, scale, a_RoundingMode);
        }

        BigDecimal fraction = new BigDecimal("0.5");
        input = input.multiply(input);
        BigDecimal resultplusfraction = result.add(fraction);
        while (((resultplusfraction).compareTo(result) == 1) && (input.compareTo(BigDecimal.ONE) == 1)) {
            if (input.compareTo(base_BigDecimal) == 1) {
                input = input.divide(base_BigDecimal, scale, a_RoundingMode);
                result = result.add(fraction);
            }
            input = input.multiply(input);
            fraction = fraction.divide(two_BigDecimal, scale, a_RoundingMode);
            resultplusfraction = result.add(fraction);
            if (fraction.abs().compareTo(maxError_BigDecimal) == -1) {
                break;
            }
            if (maxite == ite) {
                break;
            }
            ite++;
        }

        MathContext a_MathContext = new MathContext(((decimalPlaces - 1) + (result.precision() - result.scale())),
                RoundingMode.HALF_UP);
        BigDecimal roundedResult = result.round(a_MathContext);
        BigDecimal strippedRoundedResult = roundedResult.stripTrailingZeros();
        //return result;
        //return result.round(a_MathContext);
        return strippedRoundedResult;
    }
}
