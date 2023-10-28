package pl.polsl.informationtheory.fxml.controller;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.service.file.FileService;

import java.io.File;
import java.net.URL;
import java.util.List;
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

    @FXML
    private CheckMenuItem sortCountDecreasing;
    @FXML
    private CheckMenuItem sortCountIncreasing;
    @FXML
    private CheckMenuItem sortIdentifierDecreasing;
    @FXML
    private CheckMenuItem sortIdentifierIncreasing;

    @FXML
    private CheckMenuItem whitespaceCharDisplayUnicode;
    @FXML
    private CheckMenuItem whitespaceCharDisplayLabel;


    private final ReadOnlyObjectProperty<Stage> stageReadOnly;

    private final FileRepository fileRepository;

    private final FileChooser fileDialog = new FileChooser();
    private final DirectoryChooser directoryDialog = new DirectoryChooser();

    private final FileService fileService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileDialog.getExtensionFilters().addAll(AvailableFileExtensions.getAllFilters());
        sortCountDecreasing.setSelected(true);
        whitespaceCharDisplayUnicode.setSelected(true);
    }

    public void openFile() {
        probabilityTabController.defaultInit();
        List<File> files = fileDialog.showOpenMultipleDialog(stageReadOnly.get());
        fileService.openFiles(files);
    }

    public void openFolder() {
        probabilityTabController.defaultInit();
        File dir = directoryDialog.showDialog(stageReadOnly.get());
        fileService.openDir(dir);
    }

    public void sortCountDecreasing(ActionEvent event) {
        deselectAllSortButtons();
        fileRepository.setCurrentlySelectedComparator(FileRepository.ENTRY_VALUE_COMPARATOR_DECREASING);
        sortCountDecreasing.setSelected(true);
        fileRepository.sort();

    }

    public void sortCountIncreasing(ActionEvent event) {
        deselectAllSortButtons();
        fileRepository.setCurrentlySelectedComparator(FileRepository.ENTRY_VALUE_COMPARATOR_INCREASING);
        sortCountIncreasing.setSelected(true);
        fileRepository.sort();
    }

    public void sortIdentifierDecreasing(ActionEvent event) {
        deselectAllSortButtons();
        fileRepository.setCurrentlySelectedComparator(FileRepository.ENTRY_KEY_COMPARATOR_DECREASING);
        sortIdentifierDecreasing.setSelected(true);
        fileRepository.sort();
    }

    public void sortIdentifierIncreasing(ActionEvent event) {
        deselectAllSortButtons();
        fileRepository.setCurrentlySelectedComparator(FileRepository.ENTRY_KEY_COMPARATOR_INCREASING);
        sortIdentifierIncreasing.setSelected(true);
        fileRepository.sort();
    }

    private void deselectAllSortButtons() {
        sortCountDecreasing.setSelected(false);
        sortCountIncreasing.setSelected(false);
        sortIdentifierDecreasing.setSelected(false);
        sortIdentifierIncreasing.setSelected(false);
    }

    public void whitespaceCharDisplayUnicode(ActionEvent event) {
        deselectAllWhitespaceButtons();
        fileRepository.getUseUnicodeForWhitespaceCharacters().setValue(true);
        whitespaceCharDisplayUnicode.setSelected(true);
        probabilityTabController.refresh();
    }

    public void whitespaceCharDisplayLabel(ActionEvent event) {
        deselectAllWhitespaceButtons();
        fileRepository.getUseUnicodeForWhitespaceCharacters().setValue(false);
        whitespaceCharDisplayLabel.setSelected(true);
        probabilityTabController.refresh();
    }

    private void deselectAllWhitespaceButtons() {
        whitespaceCharDisplayUnicode.setSelected(false);
        whitespaceCharDisplayLabel.setSelected(false);
    }
}
