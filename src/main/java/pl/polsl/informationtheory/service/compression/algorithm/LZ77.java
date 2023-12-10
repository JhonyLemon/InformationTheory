package pl.polsl.informationtheory.service.compression.algorithm;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.service.compression.algorithm.entity.Token;

import java.util.ArrayList;
import java.util.List;

import static pl.polsl.informationtheory.service.compression.algorithm.util.TokenFlattener.flattenLZ77;

@Service
@AllArgsConstructor
public class LZ77 implements Compression {

    private static final int SEARCH_BUFFER_MAX_SIZE = 1024;
    private static final int LOOKAHEAD_BUFFER_MAX_SIZE = 16;

    @Override
    public byte[] compress(byte[] input) {
        List<Token> compressedTokens = new ArrayList<>();
        int currentIndex = 0;

        while (currentIndex < input.length) {
            int searchBufferStart = Math.max(0, currentIndex - SEARCH_BUFFER_MAX_SIZE);
            int searchBufferEnd = currentIndex;

            int bestMatchLength = 0;
            int bestMatchOffset = 0;

            for (int i = searchBufferStart; i < searchBufferEnd; i++) {
                int j = i;
                int k = currentIndex;

                int matchLength = 0;
                while (k < input.length && input[j] == input[k] && matchLength < LOOKAHEAD_BUFFER_MAX_SIZE) {
                    j++;
                    k++;
                    matchLength++;
                }

                if (matchLength > bestMatchLength) {
                    bestMatchLength = matchLength;
                    bestMatchOffset = currentIndex - i;
                }
            }

            if (bestMatchLength > 0) {
                compressedTokens.add(new Token(bestMatchOffset, bestMatchLength, input[currentIndex + bestMatchLength - 1]));
                currentIndex += bestMatchLength + 1;
            } else {
                compressedTokens.add(new Token(0, 0, input[currentIndex]));
                currentIndex++;
            }
        }

        return flattenLZ77(compressedTokens);
    }
}
