package pl.polsl.informationtheory.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.fxml.task.*;
import pl.polsl.informationtheory.repository.CompressionRepository;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.repository.ProbabilityRepository;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static javafx.concurrent.WorkerStateEvent.WORKER_STATE_SUCCEEDED;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ProbabilityRepository probabilityRepository;
    private final CompressionRepository compressionRepository;
    private final ThreadPoolTaskExecutor executor;
    private final ApplicationEventPublisher publisher;

    public List<FileInfo> getFileInfo() {
        return fileRepository.getFiles();
    }

    public void setFileInfo(Collection<FileInfo> fileInfo) {
        fileRepository.setFiles(fileInfo);
    }

    public void load(List<File> files, TaskOnProgressDataChange onUpdate) {
        LoadFileInfoTask loadFileInfoTask = new LoadFileInfoTask(files, onUpdate);
        load(onUpdate, loadFileInfoTask);
    }

    public void load(File dir, TaskOnProgressDataChange onUpdate) {
        FindFilesTask findFilesTask = new FindFilesTask(dir, onUpdate);
        findFilesTask.addEventHandler(WORKER_STATE_SUCCEEDED, e1 -> {
            LoadFileInfoTask loadFileInfoTask = new LoadFileInfoTask(findFilesTask.getValue(), onUpdate);
            load(onUpdate, loadFileInfoTask);
        });

        executor.execute(findFilesTask);
    }

    protected void load(TaskOnProgressDataChange onUpdate, LoadFileInfoTask loadFileInfoTask) {
        loadFileInfoTask.addEventHandler(WORKER_STATE_SUCCEEDED, e1 -> {
            ReadFilesTask readFilesTask = new ReadFilesTask(loadFileInfoTask.getValue(), onUpdate);
            readFilesTask.addEventHandler(WORKER_STATE_SUCCEEDED, e2 -> {
                GenerateSummedDataTask summedDataTask = new GenerateSummedDataTask(readFilesTask.getValue().values(), onUpdate);
                summedDataTask.addEventHandler(WORKER_STATE_SUCCEEDED, e3 -> {
                    SaveTask saveTask = new SaveTask(readFilesTask.getValue(), summedDataTask.getValue(), onUpdate, fileRepository, probabilityRepository);
                    saveTask.addEventHandler(WORKER_STATE_SUCCEEDED, e4 -> {
                        CompressionTask compressionTask = new CompressionTask(publisher, loadFileInfoTask.getValue(), onUpdate, compressionRepository);
                        executor.execute(compressionTask);
                    });
                    executor.execute(saveTask);
                });
                executor.execute(summedDataTask);
            });
            executor.execute(readFilesTask);
        });
        executor.execute(loadFileInfoTask);
    }
}

