package pl.polsl.informationtheory.configuration;

import javafx.fxml.FXMLLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class LoaderConfiguration {
    private final ApplicationContext applicationContext;

    public FXMLLoader fxmlLoader(URL url) {
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setControllerFactory(applicationContext::getBean);
//        fxmlLoader.setClassLoader(getApplicationContext().getClassLoader());
        return fxmlLoader;
    }

    /**
     *
     * @param viewFileName Name of the fxml file in resources folder
     * @return controller
     */
    @SuppressWarnings("unchecked")
    public FXMLLoader fxmlLoader(String viewFileName) {
        FXMLLoader loader;
        try {
            loader = fxmlLoader(applicationContext.getResource(String.format("classpath:/view/%s.fxml", viewFileName)).getURL());
            loader.load();
        } catch (Exception e) {
            log.error("Failed to load FXML file", e);
            return null;
        }
        return loader;
    }

}
