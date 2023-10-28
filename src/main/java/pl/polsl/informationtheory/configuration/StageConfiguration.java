package pl.polsl.informationtheory.configuration;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class StageConfiguration implements ApplicationListener<StageConfiguration.StageReadyEvent> {
    @Value("classpath:/view/MainView.fxml")
    private Resource mainView;
    private final ReadOnlyObjectWrapper<Stage> stage = new ReadOnlyObjectWrapper<>();
    private final LoaderConfiguration loaderConfiguration;

    @Bean
    public ReadOnlyObjectProperty<Stage> getStage() {
        return stage.getReadOnlyProperty();
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = loaderConfiguration.fxmlLoader(mainView.getURL());
            Parent parent = fxmlLoader.load();
            stage.set(event.getStage());

            Scene scene = new Scene(parent, 600, 400);
            Camera camera = new ParallelCamera();
            scene.setCamera(camera);
            stage.get().setScene(scene);

            stage.get().setTitle("Teoria Informacji Projekt");
            stage.get().setMaximized(true);
            stage.get().show();

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