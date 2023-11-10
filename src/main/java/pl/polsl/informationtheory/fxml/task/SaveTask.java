package pl.polsl.informationtheory.fxml.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.entity.SummedData;
import pl.polsl.informationtheory.enums.TaskType;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.repository.ProbabilityRepository;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SaveTask extends Task<Void> {
    private static final TaskType TYPE = TaskType.SAVING;

    private final Map<FileInfo, FileData> data;
    private final SummedData summedData;
    private final TaskOnProgressDataChange onUpdate;
    private final FileRepository fileRepository;
    private final ProbabilityRepository probabilityRepository;

    @Override
    protected void updateProgress(double v, double v1) {
        Platform.runLater(() -> onUpdate.onProgressUpdate(v/v1, TYPE));
        super.updateProgress(v, v1);
    }

    @Override
    protected void updateMessage(String s) {
        Platform.runLater(() -> onUpdate.onMessagesUpdate(s, TYPE));
        super.updateMessage(s);
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
    protected Void call() {
        log.info("Filling repositories");
        fileRepository.setFiles(data.keySet());
        updateProgress(5d,6d);
        probabilityRepository.setData(data);
        probabilityRepository.setSummedData(summedData);
        updateProgress(6d,6d);

        return null;
    }
}

