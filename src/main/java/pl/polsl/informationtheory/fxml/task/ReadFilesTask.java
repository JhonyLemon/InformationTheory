package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.TaskType;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ReadFilesTask extends Task<Map<FileInfo, FileData>> {
    private static final TaskType TYPE = TaskType.FILE_READING;

    private final List<FileInfo> fileInfos;
    private final TaskOnProgressDataChange onUpdate;

    @Override
    protected void updateProgress(double v, double v1) {
        onUpdate.onProgressUpdate(v/v1, TYPE);
        super.updateProgress(v, v1);
    }

    @Override
    protected void updateMessage(String s) {
        onUpdate.onMessagesUpdate(s, TYPE);
        super.updateMessage(s);
    }

    @Override
    protected void succeeded() {
        onUpdate.onFinish(true, TYPE);
        super.succeeded();
    }

    @Override
    protected void failed() {
        onUpdate.onFinish(false, TYPE);
        super.failed();
    }

    @Override
    protected Map<FileInfo, FileData> call() {
        Map<FileInfo, FileData> fileDataMap = new HashMap<>();

        if (Objects.isNull(fileInfos) || fileInfos.isEmpty()) {
            log.error("No valid files selected");
            updateMessage("No valid files selected");
            updateProgress(0d,1d);
            failed();
            return fileDataMap;
        }
        updateMessage("Staring to load content from files");
        log.info("Staring to load content from files");
        fileInfos.forEach(f -> {
            try {
                fileDataMap.put(
                        f,
                        SpringContext.getBean(
                                f.getExtension().getProbabilityProcessor()
                        ).probability(f)
                );

                updateMessage("Loaded file content for file "+f.getFile().getName());
                log.info("Loaded file content for file {}", f.getFile().getName());
            } catch (Exception e) {
                updateMessage("Failed to load file content");
                log.info("Failed to load file content");
                updateProgress(0d, 1);
                failed();
            }
            updateProgress(fileDataMap.size(), fileInfos.size());
        });

        updateProgress(1d,1d);
        if (fileDataMap.isEmpty()) {
            log.info("No valid files");
            updateMessage("No valid files");
            failed();
        } else {
            log.info("Loaded content of all valid files");
            updateMessage("Loaded content of all valid files");
        }
        return fileDataMap;
    }
}

