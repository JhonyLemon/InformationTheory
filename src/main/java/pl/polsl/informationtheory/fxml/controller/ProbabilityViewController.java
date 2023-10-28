package pl.polsl.informationtheory.fxml.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.SearchableComboBox;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.enums.ProbabilityType;
import pl.polsl.informationtheory.fxml.factory.MapEntryCellFactory;
import pl.polsl.informationtheory.repository.FileRepository;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class ProbabilityViewController implements Initializable {

    @FXML
    private ListView<Map.Entry<String, Integer>> probabilityList;

    @FXML
    private SearchableComboBox<String> fileSelection;

    @FXML
    private ComboBox<ProbabilityType> typeSelection;

    @FXML
    private CheckBox allFiles;

    private final FileRepository fileRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeSelection.getItems().setAll(ProbabilityType.values());
        fileSelection.setItems(fileRepository.getKeys());
        probabilityList.setCellFactory(new MapEntryCellFactory<>(fileRepository.getUseUnicodeForWhitespaceCharacters()));
        allFiles.setDisable(true);
        fileSelection.setDisable(true);
        typeSelection.setDisable(true);
        fileSelection.getItems().addListener((ListChangeListener<String>) change -> {
            if(change.getList().isEmpty()) {
                allFiles.setDisable(true);
                fileSelection.setDisable(true);
                typeSelection.setDisable(true);
            } else {
                allFiles.setDisable(false);
                typeSelection.setDisable(false);
            }
        });
        defaultInit();
    }

    public void allFiles(ActionEvent event) {
        if(allFiles.isSelected()) {
            fileSelection.setDisable(true);
            fileSelection.setValue(null);
            if(ProbabilityType.WORDS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getAllWordsCountList());
            } else if (ProbabilityType.CHARACTERS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getAllCharactersCountList());
            }
        } else {
            fileSelection.setDisable(false);
            probabilityList.setItems(null);
        }
    }

    public void typeSelection(ActionEvent event) {
        if(allFiles.isSelected()) {
            if(ProbabilityType.WORDS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getAllWordsCountList());
            } else if (ProbabilityType.CHARACTERS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getAllCharactersCountList());
            }
        } else if(isFileSelected()) {
            if(ProbabilityType.WORDS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getFiles().get(fileSelection.getValue()).getWordsCount());
            } else if (ProbabilityType.CHARACTERS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getFiles().get(fileSelection.getValue()).getCharactersCount());
            }
        }
    }

    public void fileSelection(ActionEvent event) {
        if(isFileSelected()) {
            if(ProbabilityType.WORDS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getFiles().get(fileSelection.getValue()).getWordsCount());
            } else if (ProbabilityType.CHARACTERS.equals(typeSelection.getValue())) {
                probabilityList.setItems(fileRepository.getFiles().get(fileSelection.getValue()).getCharactersCount());
            }
        }
    }

    public void refresh() {
        probabilityList.refresh();
    }

    private boolean isFileSelected() {
        return Objects.nonNull(fileSelection.getValue());
    }

    public void defaultInit() {
        typeSelection.setValue(ProbabilityType.CHARACTERS);
        probabilityList.setItems(fileRepository.getAllCharactersCountList());
        allFiles.setSelected(true);
        fileSelection.setDisable(true);
        fileSelection.setValue(null);
    }
}
