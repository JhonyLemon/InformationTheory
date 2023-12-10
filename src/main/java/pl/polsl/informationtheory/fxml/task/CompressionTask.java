package pl.polsl.informationtheory.fxml.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.ApplicationEventPublisher;
import pl.polsl.informationtheory.entity.CompressionResult;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;
import pl.polsl.informationtheory.enums.TaskType;
import pl.polsl.informationtheory.event.CompressionFinishedEvent;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.repository.CompressionRepository;
import pl.polsl.informationtheory.service.compression.algorithm.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.Files.readAllBytes;

@Slf4j
@RequiredArgsConstructor
public class CompressionTask extends Task<Void> {

    private static final Class<?>[] compressionAlgorithmClasses = {Deflate.class, Huffman.class, LZ77.class, LZ78.class, RLE.class};
    private static final TaskType TYPE = TaskType.COMPRESSION;
    private static final AtomicInteger compressedFilesCount = new AtomicInteger();
    private final ApplicationEventPublisher publisher;
    private final List<FileInfo> files;
    private final TaskOnProgressDataChange onUpdate;
    private final CompressionRepository compressionRepository;

    @Override
    protected void updateProgress(double v, double v1) {
        Platform.runLater(() -> onUpdate.onProgressUpdate(v / v1, TYPE));
        super.updateProgress(v, v1);
    }

    @Override
    protected void updateMessage(String s) {
        Platform.runLater(() -> onUpdate.onMessagesUpdate(s, TYPE));
        super.updateMessage(s);
    }

    @Override
    protected void succeeded() {
        log.info("Task: {} succeeded", TYPE);
        Platform.runLater(() -> onUpdate.onFinish(true, TYPE));
        publisher.publishEvent(new CompressionFinishedEvent());
        super.succeeded();
    }

    @Override
    protected void failed() {
        log.info("Task: {} failed", TYPE);
        Platform.runLater(() -> onUpdate.onFinish(false, TYPE));
        super.failed();
    }

    @Override
    protected Void call() {
        log.info("Compressing files");
        Map<FileInfo, List<CompressionResult>> resultsMap = Collections.synchronizedMap(new HashMap<>());
        Map<FileInfo, byte[]> filesContentMap = getFilesWithContent();

        filesContentMap.forEach((fileInfo, contentBytes) -> {
            List<CompressionResult> results = new ArrayList<>();
            Arrays.stream(compressionAlgorithmClasses).forEach(algorithmClass -> {
                CompressionResult compressionResult = prepareCompressionResult(algorithmClass, filesContentMap, fileInfo, contentBytes.length);
                results.add(compressionResult);
            });
            resultsMap.put(fileInfo, results);
        });
        compressionRepository.setCompressionResults(resultsMap);

        return null;
    }

    private Map<FileInfo, byte[]> getFilesWithContent() {
        Map<FileInfo, byte[]> fileContentMap = Collections.synchronizedMap(new HashMap<>());
        files.forEach(f -> {
            byte[] content = getFileContent(f);
            fileContentMap.put(f, content);
            updateProgress(compressedFilesCount.doubleValue(), files.size());
        });
        return fileContentMap;
    }

    private byte[] getFileContent(FileInfo fileInfo) {
        try {
            compressedFilesCount.incrementAndGet();
            if (fileInfo.getExtension().equals(AvailableFileExtensions.PDF)) {
                return readAllBytesFromPdf(fileInfo.getFile());
            } else {
                return readAllBytes(Path.of(fileInfo.getPath()));
            }
        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to load file: " + fileInfo.getPath());
        }
    }

    private byte[] readAllBytesFromPdf(File file) throws IOException {
        try (PDDocument pdf = new PDFParser(new RandomAccessReadBufferedFile(file)).parse()) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String content = pdfTextStripper.getText(pdf);
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(content);
            return buffer.array();
        }
    }

    private CompressionResult prepareCompressionResult(Class<?> algorithmClass, Map<FileInfo, byte[]> filesContentMap, FileInfo fileInfo, long initialSize) {
        try {
            Compression compressionInstance = (Compression) algorithmClass.getDeclaredConstructor().newInstance();
            byte[] content = filesContentMap.get(fileInfo);
            byte[] compressionResult = compressionInstance.compress(content);
            return new CompressionResult(algorithmClass.getSimpleName(), initialSize, (long) compressionResult.length);
        } catch (Exception e) {
            log.error("Problem with compression file with path: " + fileInfo.getPath(), e);
        }
        return null;
    }
}
