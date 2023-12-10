package pl.polsl.informationtheory.service.compression.algorithm.util;

import pl.polsl.informationtheory.service.compression.algorithm.entity.Token;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class TokenFlattener {

    public static byte[] flattenLZ77(List<Token> tokens) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (Token token : tokens) {
            outputStream.write(token.getOffset());
            outputStream.write(token.getLength());
            outputStream.write(token.getLiteral());
        }

        return outputStream.toByteArray();
    }

    public static byte[] flattenLZ78(List<Token> tokens) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (Token token : tokens) {
            outputStream.write(token.getOffset());
            outputStream.write(token.getLength());
        }

        return outputStream.toByteArray();
    }
}
