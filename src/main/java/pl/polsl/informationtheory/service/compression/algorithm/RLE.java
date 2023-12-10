package pl.polsl.informationtheory.service.compression.algorithm;

import java.io.ByteArrayOutputStream;

public class RLE implements Compression {

    @Override
    public byte[] compress(byte[] input) {
        ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();

        for (int i = 0; i < input.length; i++) {
            int runLength = 1;

            while (i + 1 < input.length && input[i] == input[i + 1]) {
                runLength++;
                i++;
            }

            compressedStream.write(runLength);
            compressedStream.write(input[i]);
        }

        return compressedStream.toByteArray();
    }
}
