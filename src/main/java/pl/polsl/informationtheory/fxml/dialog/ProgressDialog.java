package pl.polsl.informationtheory.fxml.dialog;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.exception.InformationTechnologyException;
import pl.polsl.informationtheory.fxml.task.FileTask;
import pl.polsl.informationtheory.fxml.task.TaskOnProgressDataChange;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ProgressDialog extends Dialog<FileTask> {

    private final FileTask task;
    @FXML
    private ProgressBar findFilesProgress;
    @FXML
    private ProgressBar loadFilesProgress;
    @FXML
    private ProgressBar readFilesProgress;
    @FXML
    private TreeItem<String> findMessages;
    @FXML
    private TreeItem<String> loadMessages;
    @FXML
    private TreeItem<String> readMessages;
    private StatusLight light;


    public ProgressDialog(FileTask task, Window owner) {
        this.task = task;
        try {
            Resource resource = SpringContext.getResource("classpath:/view/ProgressDialogView.fxml");
            FXMLLoader loader = new FXMLLoader(resource.getURL());
            loader.setController(this);

            DialogPane dialogPane = loader.load();

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);

            setResizable(false);
            setTitle("Loading data");

            setDialogPane(dialogPane);

            light = new StatusLight(dialogPane);

            this.task.setOnUpdate(new TaskOnProgressDataChange() {
                @Override
                public void onMessagesUpdate(String message, Integer order) {
                    Platform.runLater(() -> {
                        switch (order) {
                            case 1 -> findMessages.getChildren().add(new TreeItem<>(message));
                            case 2 -> loadMessages.getChildren().add(new TreeItem<>(message));
                            case 3 -> readMessages.getChildren().add(new TreeItem<>(message));
                            default -> log.warn("Wrong order");
                        }
                    });
                }

                @Override
                public void onProgressUpdate(Double progress, Integer order) {
                    Platform.runLater(() -> {
                        switch (order) {
                            case 1 -> findFilesProgress.setProgress(progress);
                            case 2 -> loadFilesProgress.setProgress(progress);
                            case 3 -> readFilesProgress.setProgress(progress);
                            default -> log.warn("Wrong order");
                        }
                    });
                }
            });

            dialogPane.getScene().getWindow().setOnCloseRequest(e -> {
                this.task.cancel();
                this.close();
            });

            this.task.setOnFailed(e -> light.setColor(Color.RED));
            this.task.setOnSucceeded(e ->  {
                if(this.task.getValue().isEmpty()) {
                    this.light.setColor(Color.RED);
                } else {
                    light.setColor(Color.GREEN);
                }
            });
        }
        catch (IOException e) {
            throw new InformationTechnologyException("Failed to open Progress dialog",e);
        }
    }

    public class StatusLight {
        private final Circle circle = new Circle(0,0,0,Color.TRANSPARENT);
        public StatusLight(Pane parent) {
            circle.setStroke(Color.TRANSPARENT);
            Platform.runLater(() -> {
                circle.setFill(Color.YELLOW);
                circle.setCenterX(parent.getWidth() - 8);
                circle.setCenterY(parent.getHeight() - 8);
                circle.setRadius(6);
                parent.getChildren().add(circle);
            });
        }

        public void setColor(Color color) {
            this.circle.setFill(color);
        }
    }

}
