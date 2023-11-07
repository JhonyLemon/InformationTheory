package pl.polsl.informationtheory.fxml.task;

public interface TaskProgress {
    void onNewMessage(String message);
    void onProgress(Double progress);
}
