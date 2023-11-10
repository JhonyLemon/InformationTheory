package pl.polsl.informationtheory.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ToString
@EqualsAndHashCode
public class SummedData implements WordsAndCharacters<List<Data>> {
    private final List<Data> words = new ArrayList<>();
    private final List<Data> characters = new ArrayList<>();

    public SummedData(Collection<Data> words, Collection<Data> characters) {
        this.words.addAll(words);
        this.characters.addAll(characters);
    }

    @Override
    public List<Data> getWords() {
        return words;
    }

    @Override
    public List<Data> getCharacters() {
        return characters;
    }
}
