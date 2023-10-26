package pl.polsl.informationtheory.fxml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.SearchableComboBox;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.fxml.component.LatexPaneView;
import pl.polsl.informationtheory.fxml.component.LatexView;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class EntropyViewController implements Initializable {


    @FXML
    private LatexPaneView latexPaneView;

    private LatexView latexView;

    @FXML
    private SearchableComboBox fileSelection;

    @FXML
    CheckBox allFiles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        latexView = latexPaneView.getLatexView();
    }

    public void allFiles(ActionEvent event) {

    }

    public void fileSelection(ActionEvent event) {

    }
}
