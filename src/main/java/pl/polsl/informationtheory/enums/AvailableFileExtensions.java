package pl.polsl.informationtheory.enums;

import com.google.common.io.Files;
import javafx.stage.FileChooser;
import lombok.Getter;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.service.probability.file.ProbabilityProcessor;
import pl.polsl.informationtheory.service.probability.file.ProbabilityTextFileProcessor;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AvailableFileExtensions {
    TEXT("Text Files",ProbabilityTextFileProcessor.class,"txt");

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
        return Arrays.stream(AvailableFileExtensions.values())
                .map(AvailableFileExtensions::getFilter)
                .toList();
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
