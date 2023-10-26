package pl.polsl.informationtheory.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MainViewController implements Initializable {

    @FXML
    private ProbabilityViewController probabilityTabController;

    @FXML
    private EntropyViewController entropyTabController;

    @FXML
    private CompressionViewController compressionTabController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void openFile(ActionEvent event) {

    }

    public void openFolder(ActionEvent event) {

    }

    public void openArchive(ActionEvent event) {

    }
}
