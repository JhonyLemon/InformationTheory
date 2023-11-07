package pl.polsl.informationtheory.service.probability;

import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.enums.DataType;
import pl.polsl.informationtheory.repository.FileRepository;
import pl.polsl.informationtheory.repository.ProbabilityRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void setData(Map<FileInfo, FileData> fileDataMap) {
        probabilityRepository.setData(fileDataMap);
    }

    public List<Data> getAllList(DataType type) {
        try {
            return type.getter().get(probabilityRepository.getSummedFilesData());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void sort() {

    }

}
