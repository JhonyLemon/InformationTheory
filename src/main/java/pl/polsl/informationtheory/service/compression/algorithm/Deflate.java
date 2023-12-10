package pl.polsl.informationtheory.service.compression.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

@Slf4j
public class Deflate implements Compression {

    private static final int BUFFER_SIZE = 1024;

    @Override
    public byte[] compress(byte[] input) {
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
            deflater.end();
        } catch (IOException e) {
            log.error("There was a problem with resource cleanup.", e);
        }
        return outputStream.toByteArray();
    }

}
