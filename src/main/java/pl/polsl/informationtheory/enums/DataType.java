package pl.polsl.informationtheory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.WordsAndCharacters;
import pl.polsl.informationtheory.util.interfaces.Get;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum DataType {
    CHARACTER("Characters", WordsAndCharacters::getCharacters),
    WORD("Words", WordsAndCharacters::getWords);

    private final String label;
    @Accessors(fluent = true)
    private final Get<WordsAndCharacters<List<Data>>, List<Data>> getter;

    @Override
    public String toString() {
        return label;
    }
}
