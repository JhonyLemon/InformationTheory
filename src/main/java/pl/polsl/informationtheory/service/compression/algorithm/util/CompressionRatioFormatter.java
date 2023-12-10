package pl.polsl.informationtheory.service.compression.algorithm.util;

import java.text.DecimalFormat;

public class CompressionRatioFormatter {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static String getFormatted(double compressionRatio) {
        return DECIMAL_FORMAT.format(compressionRatio);
    }
}
