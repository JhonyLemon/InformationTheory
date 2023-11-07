package pl.polsl.informationtheory.repository;

import javafx.application.Platform;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.FileInfo;
import pl.polsl.informationtheory.entity.SummedData;

import java.util.HashMap;
import java.util.Map;

@Getter
@Repository
public class ProbabilityRepository {

    private final Map<FileInfo, FileData> filesData = new HashMap<>();
    private SummedData summedFilesData;

    public void setData(Map<FileInfo, FileData> filesData) {
            this.filesData.clear();
            this.filesData.putAll(filesData);
            this.summedFilesData = new SummedData(filesData.values());
    }

}
