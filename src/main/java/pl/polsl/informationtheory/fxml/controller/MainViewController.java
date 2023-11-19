package pl.polsl.informationtheory.fxml.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;
import pl.polsl.informationtheory.event.LoadingEvent;
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

    @FXML
    private TabPane tabPane;

    @FXML
    private ProbabilityViewController probabilityTabController;
    @FXML
    private EntropyViewController entropyTabController;
    @FXML
    private CompressionViewController compressionTabController;
    @FXML
    private LoadingViewController loadingTabController;

    @FXML
    private Tab probabilityTabComp;
    @FXML
    private Tab entropyTabComp;
    @FXML
    private Tab compressionTabComp;
    @FXML
    private Tab loadingTabComp;

    @FXML
    private TextField logarithmBase;

    private final FileChooser fileDialog = new FileChooser();
    private final DirectoryChooser directoryDialog = new DirectoryChooser();

    private final ReadOnlyObjectProperty<Stage> stageReadOnly;
    private final FileService fileService;
    private final ProbabilityService probabilityService;
    private final MenuOptionsRepository menuOptionsRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileDialog.getExtensionFilters().addAll(AvailableFileExtensions.getAllFilters());
        sortCountDecreasing.setSelected(true);
        whitespaceCharDisplayUnicode.setSelected(true);
        tabPane.getTabs().clear();
        logarithmBase.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        logarithmBase.setText("2");
    }

    public void openFile() {
        List<File> files = fileDialog.showOpenMultipleDialog(stageReadOnly.get());
        loadingTabController.load(files);
        tabPane.getTabs().add(loadingTabComp);
        tabPane.getSelectionModel().select(loadingTabComp);
    }

    public void openFolder() {
        File dir = directoryDialog.showDialog(stageReadOnly.get());
        loadingTabController.load(dir);
        tabPane.getTabs().add(loadingTabComp);
        tabPane.getSelectionModel().select(loadingTabComp);
    }

    @EventListener
    public void handleSuccessful(LoadingEvent event) {
        Platform.runLater(() -> {
            Notifications
                    .create()
                    .owner(stageReadOnly.getValue())
                    .title("Loading finished")
                    .text((event.isSuccess() ? "Succeeded" : "Failed"))
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_CENTER)
                    .show();
            if(event.isSuccess()) {
                tabPane.getTabs().add(probabilityTabComp);
                tabPane.getTabs().add(entropyTabComp);
                tabPane.getTabs().add(compressionTabComp);
                probabilityTabController.defaultInit();
                entropyTabController.defaultInit();
            }
        });
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


    public void onLogarithmBaseChange(ActionEvent event) {
        menuOptionsRepository.setLogarithmBase(logarithmBase.getText());
    }
}
