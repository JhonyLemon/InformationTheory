package pl.polsl.informationtheory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProbabilityType {
    CHARACTERS("Znaki"),
    WORDS("Słowa");

    private final String label;

    @Override
    public String toString() {
        return label;
    }

}
