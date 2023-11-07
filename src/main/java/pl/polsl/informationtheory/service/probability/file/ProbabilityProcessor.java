package pl.polsl.informationtheory.service.probability.file;

import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;

import java.util.Map;

public abstract class ProbabilityProcessor {

    public abstract FileData probability(FileInfo fileInfo);



}
