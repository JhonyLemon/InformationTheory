package pl.polsl.informationtheory.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.SearchableComboBox;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class ProbabilityViewController implements Initializable {

    @FXML
    private ListView probabilityList;

    @FXML
    private SearchableComboBox fileSelection;

    @FXML
    private ComboBox typeSelection;

    @FXML
    private CheckBox allFiles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void allFiles(ActionEvent event) {

    }

    public void typeSelection(ActionEvent event) {

    }

    public void fileSelection(ActionEvent event) {

    }
}
