package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
@NoArgsConstructor
public class FindFilesTask extends Task<List<File>> implements TaskProgress {
    @Setter
    private File dir;
    @Setter
    private TaskOnProgressDataChange onUpdate;

    @Override
    public void onNewMessage(String message) {
        onUpdate.onMessagesUpdate(message, 1);
    }

    @Override
    public void onProgress(Double progress) {
        onUpdate.onProgressUpdate(progress, 1);
    }

    @Override
    protected List<File> call() {
        List<File> files = new ArrayList<>();
        if (Objects.isNull(dir) || dir.isFile()) {
            log.error("No directory selected");
            onNewMessage("No directory selected");
            onProgress(1d);
            return files;
        }
        onNewMessage("Staring to search for files in directory");
        log.info("Staring to search for files in directory {}", dir.getName());
        try {
            Files.walkFileTree(dir.toPath(), Collections.emptySet(), 10, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (isExtension(file.getFileName().toString())) {
                        File f = file.toFile();
                        files.add(f);
                        log.info("Found {} file", f.getName());
                        onNewMessage("Found " + f.getName() + " file");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    onNewMessage("Skipped " + file.toFile().getName() + " file");
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        } catch (Exception e) {
            log.error("Failed to find files in directory", e);
            onNewMessage("Failed to find files in directory");
            onProgress(0d);
            failed();
        }

        onProgress(1d);
        if(files.isEmpty()) {
            log.info("No valid files found");
            onNewMessage("No valid files found");
        } else {
            log.info("Found all valid files from directory");
            onNewMessage("Found all valid files from directory");
        }

        return files;
    }
}
