package pl.polsl.informationtheory.repository;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.entity.SummedData;

import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@Repository
public class ProbabilityRepository {

    private final Map<FileInfo, FileData> filesData = new HashMap<>();
    private final ObjectProperty<SummedData> summedData = new SimpleObjectProperty<>(null);

    public void setData(Map<FileInfo, FileData> filesData) {
            this.filesData.clear();
            this.filesData.putAll(filesData);
    }
    public void setSummedData(SummedData summedData) {
        this.summedData.setValue(summedData);
    }
}
