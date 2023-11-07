package pl.polsl.informationtheory.fxml.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.TaskProgressView;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;
import pl.polsl.informationtheory.fxml.dialog.ProgressDialog;
import pl.polsl.informationtheory.fxml.task.FileTask;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;
import pl.polsl.informationtheory.service.file.FileService;
import pl.polsl.informationtheory.service.probability.ProbabilityService;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.concurrent.WorkerStateEvent.WORKER_STATE_SUCCEEDED;

@Slf4j
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
    private final ProbabilityService probabilityService;
    private final MenuOptionsRepository menuOptionsRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileDialog.getExtensionFilters().addAll(AvailableFileExtensions.getAllFilters());
        sortCountDecreasing.setSelected(true);
        whitespaceCharDisplayUnicode.setSelected(true);
    }

    public void openFile() {
        List<File> files = fileDialog.showOpenMultipleDialog(stageReadOnly.get());
        FileTask task = new FileTask(files);
        task.addEventHandler(WORKER_STATE_SUCCEEDED, event -> {
            fileService.setFileInfo(task.getValue().keySet());
            probabilityService.setData(task.getValue());
            probabilityTabController.defaultInit();
        });
        showLoadingDialog(task);
    }

    public void openFolder() {
        File dir = directoryDialog.showDialog(stageReadOnly.get());
        FileTask task = new FileTask(dir);
        task.addEventHandler(WORKER_STATE_SUCCEEDED, event -> {
            fileService.setFileInfo(task.getValue().keySet());
            probabilityService.setData(task.getValue());
            probabilityTabController.defaultInit();
        });
        showLoadingDialog(task);
    }

    public void showLoadingDialog(FileTask task) {
        ProgressDialog progressDialog = new ProgressDialog(task, stageReadOnly.get());
        progressDialog.show();
        new Thread(task).start();
    }

    public void sortCountDecreasing(ActionEvent event) {
        deselectAllSortButtons();
        menuOptionsRepository.getCurrentlySelectedComparator().setValue(Data.Comparator.countDescending());
        sortCountDecreasing.setSelected(true);
    }

    public void sortCountIncreasing(ActionEvent event) {
        deselectAllSortButtons();
        menuOptionsRepository.getCurrentlySelectedComparator().setValue(Data.Comparator.countAscending());
        sortCountIncreasing.setSelected(true);
    }

    public void sortIdentifierDecreasing(ActionEvent event) {
        deselectAllSortButtons();
        menuOptionsRepository.getCurrentlySelectedComparator().setValue(Data.Comparator.valueDescending());
        sortIdentifierDecreasing.setSelected(true);
    }

    public void sortIdentifierIncreasing(ActionEvent event) {
        deselectAllSortButtons();
        menuOptionsRepository.getCurrentlySelectedComparator().setValue(Data.Comparator.valueAscending());
        sortIdentifierIncreasing.setSelected(true);
    }

    private void deselectAllSortButtons() {
        sortCountDecreasing.setSelected(false);
        sortCountIncreasing.setSelected(false);
        sortIdentifierDecreasing.setSelected(false);
        sortIdentifierIncreasing.setSelected(false);
    }

    public void whitespaceCharDisplayUnicode(ActionEvent event) {
        deselectAllWhitespaceButtons();
        menuOptionsRepository.useUnicode().setValue(true);
        whitespaceCharDisplayUnicode.setSelected(true);
    }

    public void whitespaceCharDisplayLabel(ActionEvent event) {
        deselectAllWhitespaceButtons();
        menuOptionsRepository.useUnicode().setValue(false);
        whitespaceCharDisplayLabel.setSelected(true);
    }

    private void deselectAllWhitespaceButtons() {
        whitespaceCharDisplayUnicode.setSelected(false);
        whitespaceCharDisplayLabel.setSelected(false);
    }
}
