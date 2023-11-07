package pl.polsl.informationtheory.fxml.task;

import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;

import java.io.File;

import java.util.*;


@Slf4j
public class FileTask extends Task<Map<FileInfo, FileData>> {
    FindFilesTask findFilesTask = new FindFilesTask();
    LoadFileInfoTask loadFileInfoTask = new LoadFileInfoTask();
    ReadFilesTask readFilesTask = new ReadFilesTask();

    private TaskOnProgressDataChange onUpdate;

    public void setOnUpdate(TaskOnProgressDataChange onUpdate) {
        this.onUpdate = onUpdate;
        this.findFilesTask.setOnUpdate(this.onUpdate);
        this.loadFileInfoTask.setOnUpdate(this.onUpdate);
        this.readFilesTask.setOnUpdate(this.onUpdate);
    }

    public FileTask(List<File> files) {
        loadFileInfoTask.addData(files);
        init();
    }

    public FileTask(File dir) {
        findFilesTask.setDir(dir);
        init();
    }

    private void init() {
        this.findFilesTask.setOnFailed(e -> this.failed());
        this.loadFileInfoTask.setOnFailed(e -> this.failed());
        this.readFilesTask.setOnFailed(e -> this.failed());
    }

    @Override
    protected Map<FileInfo, FileData> call() {
        List<File> files = findFilesTask.call();
        loadFileInfoTask.addData(files);
        List<FileInfo> fileInfos = loadFileInfoTask.call();
        readFilesTask.addData(fileInfos);
        return readFilesTask.call();
    }
}
