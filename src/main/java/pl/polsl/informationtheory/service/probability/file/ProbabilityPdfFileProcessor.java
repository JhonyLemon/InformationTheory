package pl.polsl.informationtheory.service.probability.file;

import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class ProbabilityPdfFileProcessor extends ProbabilityProcessor {

    @Override
    public FileData probability(FileInfo fileInfo) {
        try (PDDocument pdf = new PDFParser(new RandomAccessReadBufferedFile(fileInfo.getFile())).parse()){
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String content = pdfTextStripper.getText(pdf);
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(content);
            CharBuffer encodedContent = StandardCharsets.UTF_8.decode(buffer);
            return extract(encodedContent);
        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to load file");
        }
    }

}
