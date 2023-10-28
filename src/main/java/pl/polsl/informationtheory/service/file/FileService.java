package pl.polsl.informationtheory.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.service.probability.ProbabilityService;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.polsl.informationtheory.enums.AvailableFileExtensions.isExtension;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ProbabilityService probabilityService;

    public boolean openFiles(List<File> files) {
        if (Objects.isNull(files) || files.isEmpty()) {
            return false;
        }
        try {
            fileRepository.clear();
            files.forEach(fileRepository::put);
            probabilityService.calculateProbability();
        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to save file information");
        }
        return true;
    }

    public boolean openDir(File dir) {
        if (Objects.isNull(dir)) {
            return false;
        }
        Path dirPath = Path.of(dir.getAbsolutePath());
        List<File> files;
        try (Stream<Path> pathsStream = java.nio.file.Files.list(dirPath).parallel()) {
            files = new ArrayList<>(pathsStream.filter(path -> isExtension(path.getFileName().toString()))
                    .map(Path::toFile).toList());
            if (files.isEmpty()) {
                throw new InformationTechnologyException("List empty");
            }
        } catch (Exception e) {
            log.error("Failed to find files in directory", e);
            throw new InformationTechnologyException("List empty");
        }
        return openFiles(files);
    }
}

