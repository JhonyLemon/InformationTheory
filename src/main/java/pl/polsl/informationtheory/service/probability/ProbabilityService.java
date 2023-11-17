package pl.polsl.informationtheory.service.probability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;
import pl.polsl.informationtheory.repository.ProbabilityRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProbabilityService {
    private final ProbabilityRepository probabilityRepository;

    public List<Data> getList(DataType type, FileInfo fileInfo) {
        try {
            return type.getter().get(probabilityRepository.getFilesData().get(fileInfo));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Data> getAllList(DataType type) {
        try {
            return type.getter().get(probabilityRepository.getSummedData().getValue());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public BigDecimal entropyFor(List<Data> data) {
        return data.stream()
                .map(Data::getEntropy)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
