package pl.polsl.informationtheory.fxml.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.SearchableComboBox;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;
import pl.polsl.informationtheory.fxml.factory.DataCellFactory;
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
public class ProbabilityViewController implements Initializable {

    @FXML
    private ListView<Data> probabilityList;

    @FXML
    private SearchableComboBox<FileInfo> fileSelection;

    @FXML
    private ComboBox<DataType> typeSelection;

    @FXML
    private CheckBox allFiles;

    private final FileService fileService;
    private final ProbabilityService probabilityService;
    private final MenuOptionsService menuOptionsService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuOptionsService.getComparator().addListener((observableValue, dataComparator, t1) -> FXCollections.sort(probabilityList.getItems(), menuOptionsService.getComparator().getValue()));
        menuOptionsService.useUnicode().addListener((observableValue, dataComparator, t1) -> probabilityList.refresh());

        probabilityList.setCellFactory(new DataCellFactory(menuOptionsService.useUnicode()));
        typeSelection.getItems().setAll(DataType.values());

        allFiles.setDisable(true);
        fileSelection.setDisable(true);
        typeSelection.setDisable(true);
    }

    public void defaultInit() {
        allFiles.setSelected(true);
        allFiles.setDisable(false);

        typeSelection.setValue(DataType.CHARACTER);

        fileSelection.setDisable(true);
        fileSelection.setValue(null);
        fileSelection.getItems().clear();
        fileSelection.getItems().addAll(fileService.getFileInfo());

        typeSelection.setDisable(false);
        setSelectedList(probabilityService.getAllList(DataType.CHARACTER));
    }

    private void setSelectedList(List<Data> data) {
        this.probabilityList.getItems().clear();
        this.probabilityList.getItems().addAll(data);
        FXCollections.sort(probabilityList.getItems(), menuOptionsService.getComparator().getValue());
    }

    public void allFiles(ActionEvent event) {
        if(allFiles.isSelected()) {
            fileSelection.setDisable(true);
            fileSelection.setValue(null);
            setSelectedList(probabilityService.getAllList(typeSelection.getValue()));
        } else {
            fileSelection.setDisable(false);
            setSelectedList(new ArrayList<>());
        }
    }

    public void typeSelection(ActionEvent event) {
        if(allFiles.isSelected()) {
            setSelectedList(probabilityService.getAllList(typeSelection.getValue()));

        } else if(isFileSelected()) {
            setSelectedList(probabilityService.getList(typeSelection.getValue(), fileSelection.getValue()));
        }
    }

    public void fileSelection(ActionEvent event) {
        if(isFileSelected()) {
            setSelectedList(probabilityService.getList(typeSelection.getValue(), fileSelection.getValue()));
        }
    }

    private boolean isFileSelected() {
        return Objects.nonNull(fileSelection.getValue());
    }

}
