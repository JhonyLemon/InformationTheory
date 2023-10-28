package pl.polsl.informationtheory.service.probability.file;

import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.FileEntity;
import pl.polsl.informationtheory.enums.ProbabilityType;
import pl.polsl.informationtheory.exception.InformationTechnologyException;

import java.io.FileInputStream;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProbabilityTextFileProcessor extends ProbabilityProcessor {

    @Override
    public Map<ProbabilityType, Map<String, Integer>> probability(FileEntity fileEntity) {
        Map<ProbabilityType, Map<String, Integer>> probabilityTypeMap = new EnumMap<>(ProbabilityType.class);
        Map<String, Integer> wordCount = new HashMap<>();
        Map<String, Integer> characterCount = new HashMap<>();
        probabilityTypeMap.put(ProbabilityType.CHARACTERS, characterCount);
        probabilityTypeMap.put(ProbabilityType.WORDS, wordCount);
        try (FileChannel channel = new FileInputStream(fileEntity.getFile()).getChannel()) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
            StringBuilder word = new StringBuilder();
            while(charBuffer.hasRemaining()) {
                String character = String.valueOf(charBuffer.get());
                characterCount.put(character,characterCount.getOrDefault(character,0)+1);
                if((character.isBlank() || character.equals(".") || character.equals(",")) && !word.toString().isBlank()) {
                    wordCount.put(word.toString(),wordCount.getOrDefault(word.toString(),0)+1);
                    word = new StringBuilder();
                } else if(!character.isBlank() && !character.equals(".") && !character.equals(",")){
                    word.append(character);
                }
            }
        } catch (Exception e) {
            throw new InformationTechnologyException("Failed to load file");
        }
        return probabilityTypeMap;
    }



}
