package pl.polsl.informationtheory.configuration;

import javafx.fxml.FXMLLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@RequiredArgsConstructor
@Getter
public class LoaderConfiguration {
    private final ApplicationContext applicationContext;

    public FXMLLoader fxmlLoader(URL url) {
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader;
    }

}
