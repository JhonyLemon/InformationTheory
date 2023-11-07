package pl.polsl.informationtheory.fxml.task;

public interface TaskOnProgressDataChange {
    void onMessagesUpdate(String message, Integer order);
    void onProgressUpdate(Double progress, Integer order);
}
