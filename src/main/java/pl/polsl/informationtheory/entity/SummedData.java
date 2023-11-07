package pl.polsl.informationtheory.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pl.polsl.informationtheory.util.StreamHelper.toSumedData;

public class SummedData implements WordsAndCharacters<List<Data>> {
    private final List<Data> words = new ArrayList<>();
    private final List<Data> characters = new ArrayList<>();

    public SummedData(Collection<FileData> data) {



        this.words.addAll(
                data.stream()
                        .map(FileData::getWords)
                        .flatMap(List::stream)
                        .collect(toSumedData(data.stream().map(FileData::getWordsCount).mapToInt(Integer::intValue).sum()))
        );
        this.characters.addAll(
                data.stream()
                        .map(FileData::getCharacters)
                        .flatMap(List::stream)
                        .collect(toSumedData(data.stream().map(FileData::getCharactersCount).mapToInt(Integer::intValue).sum()))
        );
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
