package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.TaskType;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.repository.ProbabilityRepository;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class SaveTask extends Task<Void> {
    private static final TaskType TYPE = TaskType.SAVING;

    private final Map<FileInfo, FileData> data;
    private final TaskOnProgressDataChange onUpdate;
    private final FileRepository fileRepository;
    private final ProbabilityRepository probabilityRepository;

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
    protected Void call() {
        fileRepository.setFiles(data.keySet());
        probabilityRepository.setData(data);
        return null;
    }
}

