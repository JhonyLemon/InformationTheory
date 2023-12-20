package pl.polsl.informationtheory.enums;

import com.google.common.io.Files;
import javafx.stage.FileChooser;
import lombok.Getter;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.service.probability.file.ProbabilityDocFileProcessor;
import pl.polsl.informationtheory.service.probability.file.ProbabilityPdfFileProcessor;
import pl.polsl.informationtheory.service.probability.file.ProbabilityProcessor;
import pl.polsl.informationtheory.service.probability.file.ProbabilityTextFileProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum AvailableFileExtensions {
    TEXT("Text Files",ProbabilityTextFileProcessor.class,"txt"),
    PDF("Pdf Files",ProbabilityPdfFileProcessor.class,"pdf"),
    DOC("Doc Files", ProbabilityDocFileProcessor.class,"doc"),
    DOCX("Docx Files",ProbabilityDocFileProcessor.class,"docx"),
    EXCEL_XLSX("Excel Files",ProbabilityDocFileProcessor.class,"xlsx"),
    POWERPOINT_PPTX("Powerpoint Files",ProbabilityDocFileProcessor.class,"pptx"),
    POWERPOINT_PPT("Powerpoint Files",ProbabilityDocFileProcessor.class,"ppt");

    private final FileChooser.ExtensionFilter filter;
    private final Class<? extends ProbabilityProcessor> probabilityProcessor;
    private final List<String> extension;

    AvailableFileExtensions(String filterName, Class<? extends ProbabilityProcessor> processor, String... extension) {
        List<String> extensions = Arrays.stream(extension).toList();
        this.filter = new FileChooser.ExtensionFilter(filterName, extensions.stream().map(e -> "*."+e).toList());
        this.probabilityProcessor = processor;
        this.extension = Arrays.stream(extension).toList();
    }

    public static List<FileChooser.ExtensionFilter> getAllFilters() {
        List<FileChooser.ExtensionFilter> extensionFilters = new ArrayList<>();
        extensionFilters.add(new FileChooser.ExtensionFilter(
                "All",
                Arrays.stream(AvailableFileExtensions.values())
                        .map(AvailableFileExtensions::getExtension)
                        .flatMap(List::stream)
                        .map(e -> "*."+e)
                        .toList()
        ));
        extensionFilters.addAll(Arrays.stream(AvailableFileExtensions.values())
                .map(AvailableFileExtensions::getFilter).toList());
        return extensionFilters;
    }

    public static List<String> getAllExtensions() {
        return Arrays.stream(AvailableFileExtensions.values())
                .map(AvailableFileExtensions::getExtension)
                .flatMap(List::stream)
                .toList();
    }

    public static boolean isExtension(String fileName) {
        return getAllExtensions().contains(Files.getFileExtension(fileName));
    }

    public static AvailableFileExtensions extensionType(String fileName) {
        String ext = Files.getFileExtension(fileName);
        return Arrays.stream(AvailableFileExtensions.values())
                .filter(e -> e.extension.contains(ext))
                .findFirst()
                .orElseThrow(() -> new InformationTechnologyException("Extension not found"));

    }

}
