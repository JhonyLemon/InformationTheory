package pl.polsl.informationtheory.service.probability.file;

import pl.polsl.informationtheory.entity.FileEntity;
import pl.polsl.informationtheory.enums.ProbabilityType;
import pl.polsl.informationtheory.enums.WhitespaceCharacters;

import java.util.Map;

public abstract class ProbabilityProcessor {

    public abstract Map<ProbabilityType, Map<String, Integer>> probability(FileEntity fileEntity);



}
