package pl.polsl.informationtheory.service.probability.file;

import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;

import java.io.FileInputStream;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Service
public class ProbabilityTextFileProcessor extends ProbabilityProcessor {

    @Override
    public FileData probability(FileInfo fileInfo) {
        try (FileInputStream fileInputStream = new FileInputStream(fileInfo.getFile())) {
            FileChannel channel = fileInputStream.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
            return extract(charBuffer);
        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to load file");
        }
    }
}
