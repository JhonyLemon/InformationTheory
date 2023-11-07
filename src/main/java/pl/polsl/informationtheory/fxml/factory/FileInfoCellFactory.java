package pl.polsl.informationtheory.fxml.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import pl.polsl.informationtheory.entity.FileInfo;

public class FileInfoCellFactory implements Callback<ListView<FileInfo>, ListCell<FileInfo>> {
    @Override
    public ListCell<FileInfo> call(ListView<FileInfo> fileInfoListView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(FileInfo fileInfo, boolean b) {
                super.updateItem(fileInfo, b);
                setText(fileInfo.getPath());
            }
        };
    }
}
