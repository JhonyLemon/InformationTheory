package pl.polsl.informationtheory.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.FileInfo;

import java.util.*;

@Getter
@Repository
public class FileRepository {
//    public static final Comparator ENTRY_VALUE_COMPARATOR_INCREASING = Map.Entry.comparingByValue();
//    public static final Comparator ENTRY_VALUE_COMPARATOR_DECREASING = Map.Entry.comparingByValue().reversed();
//    public static final Comparator ENTRY_KEY_COMPARATOR_INCREASING = Map.Entry.comparingByKey();
//    public static final Comparator ENTRY_KEY_COMPARATOR_DECREASING = Map.Entry.comparingByKey().reversed();

    private final List<FileInfo> files = new ArrayList<>();

    public void setFiles(Collection<FileInfo> files) {
            this.files.clear();
            this.files.addAll(
                    files
            );
    }

//    public void sort() {
//        FXCollections.sort(allWordsCountList, currentlySelectedComparator);
//        FXCollections.sort(allCharactersCountList, currentlySelectedComparator);
//        files.values().forEach(fileInfo -> {
//            FXCollections.sort(fileInfo.getCharactersCount(), currentlySelectedComparator);
//            FXCollections.sort(fileInfo.getWordsCount(), currentlySelectedComparator);
//        });
//    }
}
