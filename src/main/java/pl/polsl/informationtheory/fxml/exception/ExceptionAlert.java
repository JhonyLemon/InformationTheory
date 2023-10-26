package pl.polsl.informationtheory.fxml.exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ExceptionAlert extends Alert {

    public ExceptionAlert(Thread thread, Throwable throwable) {
        super(AlertType.ERROR);
        this.setTitle("Exception Dialog");
        this.setHeaderText("Error");

        Throwable targetException = ((InvocationTargetException) throwable.getCause()).getTargetException();

        if(targetException instanceof InformationTechnologyException) {
            this.setContentText(targetException.getMessage());
            log.error(targetException.getMessage());
        } else {
            this.setContentText("Unknown error: "+targetException.getMessage());
            log.error(targetException.getMessage());
        }

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        targetException.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        this.getDialogPane().setExpandableContent(expContent);

        this.showAndWait();
    }

}
