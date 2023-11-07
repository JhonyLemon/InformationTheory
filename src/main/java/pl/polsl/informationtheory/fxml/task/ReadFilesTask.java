package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;

import java.util.*;

@Slf4j
@NoArgsConstructor
public class ReadFilesTask extends Task<Map<FileInfo, FileData>> implements TaskProgress {
    private List<FileInfo> fileInfos = new ArrayList<>();
    @Setter
    private TaskOnProgressDataChange onUpdate;

    @Override
    public void onNewMessage(String message) {
        onUpdate.onMessagesUpdate(message, 3);
    }

    @Override
    public void onProgress(Double progress) {
        onUpdate.onProgressUpdate(progress, 3);
    }

    public void addData(List<FileInfo> fileInfos) {
        this.fileInfos.addAll(fileInfos);
    }

    @Override
    protected Map<FileInfo, FileData> call() {
        Map<FileInfo, FileData> fileDataMap = new HashMap<>();

        if (Objects.isNull(fileInfos) || fileInfos.isEmpty()) {
            log.error("No valid files selected");
            onNewMessage("No valid files selected");
            onProgress(0d);
            failed();
            return fileDataMap;
        }
        onNewMessage("Staring to load content from files");
        log.info("Staring to load content from files");
        fileInfos.forEach(f -> {
            try {
                fileDataMap.put(
                        f,
                        SpringContext.getBean(
                                f.getExtension().getProbabilityProcessor()
                        ).probability(f)
                );

                onNewMessage("Loaded file content for file "+f.getFile().getName());
                log.info("Loaded file content for file {}", f.getFile().getName());
            } catch (Exception e) {
                onNewMessage("Failed to load file content");
                log.info("Failed to load file content");
                onProgress(0d);
                failed();
            }
            onProgress((double)fileDataMap.size()/(double)fileInfos.size());
        });

        onProgress(1d);
        if (fileDataMap.isEmpty()) {
            log.info("No valid files");
            onNewMessage("No valid files");
            failed();
        } else {
            log.info("Loaded content of all valid files");
            onNewMessage("Loaded content of all valid files");
        }
        return fileDataMap;
    }
}

