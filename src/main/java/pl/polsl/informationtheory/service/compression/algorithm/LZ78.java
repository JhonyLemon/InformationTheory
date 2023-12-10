package pl.polsl.informationtheory.service.compression.algorithm;

import pl.polsl.informationtheory.service.compression.algorithm.entity.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.polsl.informationtheory.service.compression.algorithm.util.TokenFlattener.flattenLZ78;

public class LZ78 implements Compression {

    @Override
    public byte[] compress(byte[] input) {
        Map<String, Integer> dictionary = new HashMap<>();
        List<Token> compressedTokens = new ArrayList<>();

        int code = 0;
        StringBuilder currentPhrase = new StringBuilder();

        for (byte b : input) {
            currentPhrase.append((char) b);

            if (!dictionary.containsKey(currentPhrase.toString())) {
                // Output the code for the current phrase (if it exists)
                compressedTokens.add(new Token(code, b));

                // Add the new phrase to the dictionary
                dictionary.put(currentPhrase.toString(), ++code);

                // Reset the current phrase
                currentPhrase.setLength(0);
            }
        }

        // Output the last code (if it exists)
        if (!currentPhrase.isEmpty()) {
            compressedTokens.add(new Token(code, (byte) 0)); // The last character is not part of the dictionary
        }

        return flattenLZ78(compressedTokens);
    }
}
