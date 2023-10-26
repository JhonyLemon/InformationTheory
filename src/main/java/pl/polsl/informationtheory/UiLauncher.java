package pl.polsl.informationtheory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.polsl.informationtheory.configuration.StageConfiguration;
import pl.polsl.informationtheory.fxml.exception.ExceptionAlert;

public class UiLauncher extends Application{
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(InformationTheoryApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> new ExceptionAlert(t, e)));
//        Thread.currentThread().setUncaughtExceptionHandler(ExceptionAlert::new);
        applicationContext.publishEvent(new StageConfiguration.StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}