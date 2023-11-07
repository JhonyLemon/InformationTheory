package pl.polsl.informationtheory.fxml.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TaskProgressData {
    private Double progress;
    private final List<String> messages;
    private final Integer order;
}
