package pl.polsl.informationtheory.service.probability.file;

import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class ProbabilityDocFileProcessor extends ProbabilityProcessor {

    @Override
    public FileData probability(FileInfo fileInfo) {
        try (POITextExtractor oleTextExtractor = ExtractorFactory.createExtractor(fileInfo.getFile())) {
                String content = oleTextExtractor.getText();
                ByteBuffer buffer = StandardCharsets.UTF_8.encode(content);
                CharBuffer encodedContent = StandardCharsets.UTF_8.decode(buffer);
                return extract(encodedContent);
        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to load file");
        }
    }

}
