package pl.polsl.informationtheory.service.file;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.service.probability.ProbabilityService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

import static pl.polsl.informationtheory.enums.AvailableFileExtensions.isExtension;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ProbabilityService probabilityService;


    public List<FileInfo> getFileInfo() {
        return fileRepository.getFiles();
    }

    public void setFileInfo(Collection<FileInfo> fileInfo) {
        fileRepository.setFiles(fileInfo);
    }



}

