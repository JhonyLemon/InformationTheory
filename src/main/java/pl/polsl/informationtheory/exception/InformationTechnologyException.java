package pl.polsl.informationtheory.exception;

import lombok.Getter;

@Getter
public class InformationTechnologyException extends RuntimeException {
    protected final String message;
    protected final Exception parentException;

    public InformationTechnologyException(String message, Exception parentException) {
        super(message);
        this.message = message;
        this.parentException = parentException;
    }

    public InformationTechnologyException(String message) {
        super(message);
        this.message = message;
        this.parentException = this;
    }
}