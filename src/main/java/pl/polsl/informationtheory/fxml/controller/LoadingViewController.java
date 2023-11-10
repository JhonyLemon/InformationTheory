package pl.polsl.informationtheory.fxml.controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.enums.TaskType;
import pl.polsl.informationtheory.event.LoadingEvent;
import pl.polsl.informationtheory.fxml.task.TaskOnProgressDataChange;
import pl.polsl.informationtheory.service.file.FileService;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadingViewController implements Initializable {

    @FXML
    private ProgressBar findFilesProgress;
    @FXML
    private ProgressBar loadFilesProgress;
    @FXML
    private ProgressBar readFilesProgress;

    @FXML
    private Label findFilesLabel;
    @FXML
    private Label loadFilesLabel;
    @FXML
    private Label readFilesLabel;

    @FXML
    private TreeItem<String> messages;
    @FXML
    private TreeItem<String> findMessages;
    @FXML
    private TreeItem<String> loadMessages;
    @FXML
    private TreeItem<String> readMessages;

    private final BooleanProperty loadingDir = new SimpleBooleanProperty(true);

    private final FileService fileService;
    private final ApplicationEventPublisher publisher;

    private final TaskOnProgressDataChange onUpdate = new TaskOnProgressDataChange() {
        @Override
        public void onMessagesUpdate(String message, TaskType task) {
            Platform.runLater(() -> {
                switch (task) {
                    case DIR_SEARCHING -> findMessages.getChildren().add(new TreeItem<>(message));
                    case FILE_LOADING -> loadMessages.getChildren().add(new TreeItem<>(message));
                    case FILE_READING -> readMessages.getChildren().add(new TreeItem<>(message));
                    default -> log.warn("Wrong order");
                }
            });
        }

        @Override
        public void onProgressUpdate(Double progress, TaskType task) {
            Platform.runLater(() -> {
                switch (task) {
                    case DIR_SEARCHING -> findFilesProgress.setProgress(progress);
                    case FILE_LOADING -> loadFilesProgress.setProgress(progress);
                    case FILE_READING -> readFilesProgress.setProgress(progress);
                    default -> log.warn("Wrong order");
                }
            });
        }

        @Override
        public void onFinish(boolean succeeded, TaskType task) {
            if (task.equals(TaskType.FILE_READING)) {
                publisher.publishEvent(new LoadingEvent(succeeded));
            }
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        findFilesLabel.visibleProperty().bind(loadingDir);
        findFilesLabel.managedProperty().bind(loadingDir);
        findFilesProgress.visibleProperty().bind(loadingDir);
        findFilesProgress.managedProperty().bind(loadingDir);

        messages.getChildren().clear();
    }

    public void load(File dir) {
        clearOutput();

        this.messages.getChildren().add(findMessages);
        this.messages.getChildren().add(loadMessages);
        this.messages.getChildren().add(readMessages);

        loadingDir.set(true);
        fileService.load(dir, onUpdate);
    }

    public void load(List<File> files) {
        clearOutput();

        this.messages.getChildren().add(loadMessages);
        this.messages.getChildren().add(readMessages);

        loadingDir.set(false);
        fileService.load(files, onUpdate);
    }

    private void clearOutput() {
        this.findFilesProgress.setProgress(-1);
        this.loadFilesProgress.setProgress(0);
        this.readFilesProgress.setProgress(0);
        this.messages.getChildren().clear();
        this.findMessages.getChildren().clear();
        this.loadMessages.getChildren().clear();
        this.readMessages.getChildren().clear();
    }

}
