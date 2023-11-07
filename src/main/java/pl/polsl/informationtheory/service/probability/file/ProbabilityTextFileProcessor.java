package pl.polsl.informationtheory.service.probability.file;

import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.io.FileInputStream;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProbabilityTextFileProcessor extends ProbabilityProcessor {

    @Override
    public FileData probability(FileInfo fileInfo) {
        Map<String, Data> words = new HashMap<>();
        Map<String, Data> characters = new HashMap<>();
        AtomicInteger charactersCount = new AtomicInteger(0);
        AtomicInteger wordsCount = new AtomicInteger(0);

        try (FileChannel channel = new FileInputStream(fileInfo.getFile()).getChannel()) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
            StringBuilder word = new StringBuilder();
            while(charBuffer.hasRemaining()) {
                String character = String.valueOf(charBuffer.get());

                charactersCount.addAndGet(1);
                if(characters.containsKey(character)) {
                    characters.get(character).addCount(1);
                } else {
                    characters.put(character, new Data(DataType.CHARACTER, character, 1));
                }

                if((character.isBlank() || character.equals(".") || character.equals(",")) && !word.toString().isBlank()) {

                    wordsCount.addAndGet(1);
                    if(words.containsKey(word.toString())) {
                        words.get(word.toString()).addCount(1);
                    } else {
                        words.put(word.toString(), new Data(DataType.WORD, word.toString(), 1));
                    }

                    word = new StringBuilder();
                } else if(!character.isBlank() && !character.equals(".") && !character.equals(",")){
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

        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to load file");
        }
        return new FileData(words.values(), characters.values());
    }



}
