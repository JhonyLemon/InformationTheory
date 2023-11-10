package pl.polsl.informationtheory.service.file;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.fxml.task.*;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.repository.ProbabilityRepository;
import pl.polsl.informationtheory.service.probability.ProbabilityService;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ProbabilityRepository probabilityRepository;
    private final ProbabilityService probabilityService;


    public List<FileInfo> getFileInfo() {
        return fileRepository.getFiles();
    }

    public void setFileInfo(Collection<FileInfo> fileInfo) {
        fileRepository.setFiles(fileInfo);
    }

    @Async
    public void load(List<File> files, TaskOnProgressDataChange onUpdate) {
        LoadFileInfoTask loadFileInfoTask = new LoadFileInfoTask(files, onUpdate);
        load(onUpdate, loadFileInfoTask);
    }

    @Async
    public void load(File dir, TaskOnProgressDataChange onUpdate) {
        FindFilesTask findFilesTask = new FindFilesTask(dir, onUpdate);
        findFilesTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, e1 -> {
            LoadFileInfoTask loadFileInfoTask = new LoadFileInfoTask(findFilesTask.getValue(), onUpdate);
            load(onUpdate, loadFileInfoTask);
        });
        findFilesTask.run();
    }

    private void load(TaskOnProgressDataChange onUpdate, LoadFileInfoTask loadFileInfoTask) {
        loadFileInfoTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, e2 -> {
            ReadFilesTask readFilesTask = new ReadFilesTask(loadFileInfoTask.getValue(), onUpdate);
            readFilesTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, e3 -> {
                SaveTask saveTask = new SaveTask(readFilesTask.getValue(), onUpdate, fileRepository, probabilityRepository);
                saveTask.run();
            });
            readFilesTask.run();
        });
        loadFileInfoTask.run();
    }

}

