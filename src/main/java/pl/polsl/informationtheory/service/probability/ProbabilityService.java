package pl.polsl.informationtheory.service.probability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.enums.ProbabilityType;
import pl.polsl.informationtheory.repository.FileRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProbabilityService {

    private final FileRepository fileRepository;

    public void calculateProbability() {
        fileRepository.getFiles().values().forEach(
                file -> {
                    Map<ProbabilityType, Map<String, Integer>> probabilityTypeMap = SpringContext.getBean(
                            file.getExtension().getProbabilityProcessor()
                    ).probability(file);
                    file.getWords().putAll(probabilityTypeMap.get(ProbabilityType.WORDS));
                    file.getCharacters().putAll(probabilityTypeMap.get(ProbabilityType.CHARACTERS));
                    fileRepository.sort();
                }
        );
    }

}
