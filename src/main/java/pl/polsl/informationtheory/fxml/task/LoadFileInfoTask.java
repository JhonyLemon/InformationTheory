package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class LoadFileInfoTask extends Task<List<FileInfo>> implements TaskProgress {
    private List<File> files = new ArrayList<>();
    @Setter
    private TaskOnProgressDataChange onUpdate;

    @Override
    public void onNewMessage(String message) {
        onUpdate.onMessagesUpdate(message, 2);
    }

    @Override
    public void onProgress(Double progress) {
        onUpdate.onProgressUpdate(progress, 2);
    }

    public void addData(List<File> files) {
        this.files.addAll(files);
    }

    @Override
    protected List<FileInfo> call() {
        List<FileInfo> fileInfos = new ArrayList<>();
        if (Objects.isNull(files) || files.isEmpty()) {
            log.error("No valid files selected");
            onNewMessage("No valid files selected");
            onProgress(0d);
            failed();
            return fileInfos;
        }
        log.info("Saving files information");
        onNewMessage("Saving files information");

        files.stream().filter(f -> AvailableFileExtensions.isExtension(f.getName()))
                        .forEach(f -> {
                            fileInfos.add(new FileInfo(
                                    f.getAbsolutePath(),
                                    AvailableFileExtensions.extensionType(f.getName())
                            ));
                            log.info("Saved information for {} file", f.getAbsolutePath());
                            onNewMessage("Saved information for " + f.getName() + " file");
                            onProgress((double)fileInfos.size()/(double)files.size());
                        });

        if(fileInfos.isEmpty()) {
            log.info("No valid files");
            onNewMessage("No valid files");
            onProgress(0d);
            failed();
        } else {
            onProgress(1d);
            log.info("Saved information for all valid files");
            onNewMessage("Saved information for all valid files");
        }
        return fileInfos;
    }
}
