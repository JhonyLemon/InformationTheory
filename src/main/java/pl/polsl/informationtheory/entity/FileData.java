package pl.polsl.informationtheory.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileData implements WordsAndCharacters<List<Data>> {
    private final List<Data> words = new ArrayList<>();
    private final List<Data> characters = new ArrayList<>();
    @Getter
    private final Integer wordsCount;
    @Getter
    private final Integer charactersCount;

    public FileData(Collection<Data> words, Collection<Data> characters) {
        this.words.addAll(words);
        this.characters.addAll(characters);
        this.wordsCount = this.words.stream().findAny().map(Data::getCountAll).orElse(0);
        this.charactersCount = this.characters.stream().findAny().map(Data::getCountAll).orElse(0);
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
