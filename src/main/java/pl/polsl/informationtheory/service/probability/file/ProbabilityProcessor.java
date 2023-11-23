package pl.polsl.informationtheory.service.probability.file;

import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ProbabilityProcessor {

    public static final List<String> END_OF_WORD_CHARACTER = List.of(".", ",", "?", "!");

    public abstract FileData probability(FileInfo fileInfo);

    protected FileData extract(CharBuffer encodedContent) {
        Map<String, Data> words = new HashMap<>();
        Map<String, Data> characters = new HashMap<>();
        AtomicInteger charactersCount = new AtomicInteger(0);
        AtomicInteger wordsCount = new AtomicInteger(0);

        StringBuilder word = new StringBuilder();
        while(encodedContent.hasRemaining()) {
            String character = String.valueOf(encodedContent.get());

            charactersCount.addAndGet(1);
            put(characters,character, DataType.CHARACTER);

            if(isEndOfWord(character, word)) {
                wordsCount.addAndGet(1);
                put(words, word.toString(), DataType.WORD);
                word = new StringBuilder();
            } else if(isPartOfWord(character)) {
                word.append(character);
            }
        }
        characters.values().forEach(c -> {
            c.setCountAll(charactersCount.get());
            c.initialize();
        });
        words.values().forEach(c -> {
            c.setCountAll(wordsCount.get());
            c.initialize();
        });

        return new FileData(words.values(), characters.values());
    }

    private void put(Map<String, Data> map,String key, DataType dataType) {
        if(map.containsKey(key)) {
            map.get(key).addCount(1);
        } else {
            map.put(key, new Data(dataType, key, 1));
        }
    }

    private boolean isEndOfWord(String character, StringBuilder word) {
        return (character.isBlank() || isEndOfWordCharacter(character)) &&
                !word.toString().isBlank();
    }

    private boolean isPartOfWord(String character) {
        return !character.isBlank() &&
                !isEndOfWordCharacter(character);
    }

    private boolean isEndOfWordCharacter(String character) {
        return END_OF_WORD_CHARACTER.contains(character);
    }

}
