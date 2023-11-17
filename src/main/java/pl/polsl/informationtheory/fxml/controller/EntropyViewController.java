package pl.polsl.informationtheory.fxml.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.SearchableComboBox;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;
import pl.polsl.informationtheory.fxml.factory.EntropyDataCellFactory;
import pl.polsl.informationtheory.service.MenuOptionsService;
import pl.polsl.informationtheory.service.file.FileService;
import pl.polsl.informationtheory.service.probability.ProbabilityService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class EntropyViewController implements Initializable {

    @FXML
    private ListView<Data> entropyList;

    @FXML
    private Label entropyForFile;

    @FXML
    private SearchableComboBox<FileInfo> fileSelection;

    @FXML
    CheckBox allFiles;

    private final FileService fileService;
    private final ProbabilityService probabilityService;
    private final MenuOptionsService menuOptionsService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuOptionsService.useUnicode().addListener((observableValue, dataComparator, t1) -> entropyList.refresh());

        entropyList.setCellFactory(new EntropyDataCellFactory(menuOptionsService.useUnicode()));

        allFiles.setDisable(true);
        fileSelection.setDisable(true);
    }

    public void defaultInit() {
        allFiles.setSelected(true);
        allFiles.setDisable(false);

        fileSelection.setDisable(true);
        fileSelection.setValue(null);
        fileSelection.getItems().clear();
        fileSelection.getItems().addAll(fileService.getFileInfo());

        setSelectedList(probabilityService.getAllList(DataType.CHARACTER));
    }

    private void setSelectedList(List<Data> data) {
        this.entropyList.getItems().clear();
        this.entropyList.getItems().addAll(data);
        FXCollections.sort(entropyList.getItems(), Data.Comparator.valueAscending());
        entropyForFile.setText(probabilityService.entropyFor(data).toString());
    }


    public void allFiles(ActionEvent event) {
        if(allFiles.isSelected()) {
            fileSelection.setDisable(true);
            fileSelection.setValue(null);
            setSelectedList(probabilityService.getAllList(DataType.CHARACTER));
        } else {
            fileSelection.setDisable(false);
            setSelectedList(new ArrayList<>());
        }
    }

    public void fileSelection(ActionEvent event) {
        if(isFileSelected()) {
            setSelectedList(probabilityService.getList(DataType.CHARACTER, fileSelection.getValue()));
        }
    }

    private boolean isFileSelected() {
        return Objects.nonNull(fileSelection.getValue());
    }

}
