package pl.polsl.informationtheory.fxml.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.enums.TaskType;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static pl.polsl.informationtheory.enums.AvailableFileExtensions.isExtension;

@Slf4j
@RequiredArgsConstructor
public class FindFilesTask extends Task<List<File>> {
    private static final TaskType TYPE = TaskType.DIR_SEARCHING;

    private final File dir;
    private final TaskOnProgressDataChange onUpdate;

    @Override
    protected void updateMessage(String s) {
        Platform.runLater(() -> onUpdate.onMessagesUpdate(s, TYPE));
        super.updateMessage(s);
    }

    @Override
    protected void updateProgress(double v, double v1) {
        Platform.runLater(() -> onUpdate.onProgressUpdate(v/v1, TYPE));
        super.updateProgress(v, v1);
    }

    @Override
    protected void succeeded() {
        log.info("Task: {} succeeded", TYPE);
        Platform.runLater(() -> onUpdate.onFinish(true, TYPE));
        super.succeeded();
    }

    @Override
    protected void failed() {
        log.info("Task: {} failed", TYPE);
        Platform.runLater(() -> onUpdate.onFinish(false, TYPE));
        super.failed();
    }

    @Override
    protected List<File> call() {
        List<File> files = new ArrayList<>();
        if (Objects.isNull(dir) || dir.isFile()) {
            log.error("No directory selected");
            updateMessage("No directory selected");
            updateProgress(1d,1d);
            return files;
        }
        updateMessage("Staring to search for files in directory");
        log.info("Staring to search for files in directory {}", dir.getName());
        try {
            Files.walkFileTree(dir.toPath(), Collections.emptySet(), 10, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (isExtension(file.getFileName().toString())) {
                        File f = file.toFile();
                        files.add(f);
                        log.info("Found {} file", f.getName());
                        updateMessage("Found " + f.getName() + " file");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    log.info("Skipped {} file", file.toFile().getName());
                    updateMessage("Skipped " + file.toFile().getName() + " file");
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        } catch (Exception e) {
            log.error("Failed to find files in directory", e);
            updateMessage("Failed to find files in directory");
            updateProgress(0d,1d);
            failed();
        }

        updateProgress(1d, 1);
        if(files.isEmpty()) {
            log.info("No valid files found");
            updateMessage("No valid files found");
        } else {
            log.info("Found all valid files from directory");
            updateMessage("Found all valid files from directory");
        }

        return files;
    }
}
