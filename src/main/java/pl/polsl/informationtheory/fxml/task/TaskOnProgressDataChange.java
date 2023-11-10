package pl.polsl.informationtheory.fxml.task;

import pl.polsl.informationtheory.enums.TaskType;

public interface TaskOnProgressDataChange {
    void onMessagesUpdate(String message, TaskType task);
    void onProgressUpdate(Double progress, TaskType task);

    void onFinish(boolean succeeded, TaskType task);
}
