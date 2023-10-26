package pl.polsl.informationtheory.configuration;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class StageConfiguration implements ApplicationListener<StageConfiguration.StageReadyEvent> {
    @Value("classpath:/view/MainView.fxml")
    private Resource mainView;
    private Stage stage;
    private final LoaderConfiguration loaderConfiguration;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = loaderConfiguration.fxmlLoader(mainView.getURL());
            Parent parent = fxmlLoader.load();
            stage = event.getStage();

            Scene scene = new Scene(parent, 600, 400);
            Camera camera = new ParallelCamera();
            scene.setCamera(camera);
            stage.setScene(scene);

            stage.setTitle("Teoria Informacji Projekt");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            log.error("Failed to start application", e);
            throw new InformationTechnologyException("Failed to load",e);
        }
    }

    public static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage(){
            return ((Stage) getSource());
        }
    }
}