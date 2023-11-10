package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;
import pl.polsl.informationtheory.enums.TaskType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class LoadFileInfoTask extends Task<List<FileInfo>> {
    private static final TaskType TYPE = TaskType.FILE_LOADING;

    private final List<File> files;
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
    protected List<FileInfo> call() {
        List<FileInfo> fileInfos = new ArrayList<>();
        if (Objects.isNull(files) || files.isEmpty()) {
            log.error("No valid files selected");
            updateMessage("No valid files selected");
            updateProgress(0d,1);
            failed();
            return fileInfos;
        }
        log.info("Saving files information");
        updateMessage("Saving files information");

        files.stream().filter(f -> AvailableFileExtensions.isExtension(f.getName()))
                        .forEach(f -> {
                            fileInfos.add(new FileInfo(
                                    f.getAbsolutePath(),
                                    AvailableFileExtensions.extensionType(f.getName())
                            ));
                            log.info("Saved information for {} file", f.getAbsolutePath());
                            updateMessage("Saved information for " + f.getName() + " file");
                            updateProgress(fileInfos.size(),files.size());
                        });

        if(fileInfos.isEmpty()) {
            log.info("No valid files");
            updateMessage("No valid files");
            updateProgress(0d,1d);
            failed();
        } else {
            updateProgress(1d,1);
            log.info("Saved information for all valid files");
            updateMessage("Saved information for all valid files");
        }
        return fileInfos;
    }
}
