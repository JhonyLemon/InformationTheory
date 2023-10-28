package pl.polsl.informationtheory.repository;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import pl.polsl.informationtheory.entity.FileEntity;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Repository
@Getter
public class FileRepository {
    public static final Comparator ENTRY_VALUE_COMPARATOR_INCREASING = Map.Entry.comparingByValue();
    public static final Comparator ENTRY_VALUE_COMPARATOR_DECREASING = Map.Entry.comparingByValue().reversed();
    public static final Comparator ENTRY_KEY_COMPARATOR_INCREASING = Map.Entry.comparingByKey();
    public static final Comparator ENTRY_KEY_COMPARATOR_DECREASING = Map.Entry.comparingByKey().reversed();
    @Setter
    private Comparator currentlySelectedComparator = ENTRY_VALUE_COMPARATOR_DECREASING;

    private BooleanProperty useUnicodeForWhitespaceCharacters = new SimpleBooleanProperty(true);

    private final ObservableMap<String, FileEntity> files = new ObservableMapWrapper<>(new HashMap<>());

    private final ObservableList<Map.Entry<String, Integer>> allWordsCountList = new ObservableListWrapper<>(new ArrayList<>());
    private final ObservableMap<String, Integer> allWordsCountMap = new ObservableMapWrapper<>(new HashMap<>());

    private final ObservableList<Map.Entry<String, Integer>> allCharactersCountList = new ObservableListWrapper<>(new ArrayList<>());
    private final ObservableMap<String, Integer> allCharactersCountMap = new ObservableMapWrapper<>(new HashMap<>());

    private final ObservableList<String> keys = new ObservableListWrapper<>(new ArrayList<>());

    private final MapChangeListener<String, Integer> wordsChangeListener = (change) -> {
        if(change.wasAdded()) {
            allWordsCountMap.put(change.getKey(), allWordsCountMap.getOrDefault(change.getKey(), 0)+change.getValueAdded());
        }
    };

    private final MapChangeListener<String, Integer> charactersChangeListener = (change) -> {
        if(change.wasAdded()) {
            allCharactersCountMap.put(change.getKey(), allCharactersCountMap.getOrDefault(change.getKey(), 0)+change.getValueAdded());
        }
    };

    private final MapChangeListener<String, Integer> wordsChangeListenerList = (change) -> {
        allWordsCountList.clear();
        allWordsCountList.addAll(allWordsCountMap.entrySet());
    };

    private final MapChangeListener<String, Integer> charactersChangeListenerList = (change) -> {
        allCharactersCountList.clear();
        allCharactersCountList.addAll(allCharactersCountMap.entrySet());
    };

    private final MapChangeListener<String, FileEntity> keysChangeListener = change -> {
        if(change.wasAdded()) {
            keys.add(change.getKey());
        } else if (change.wasRemoved()) {
            keys.remove(Map.entry(change.getKey(), change.getValueRemoved()));
        }
    };

    public FileRepository() {
        files.addListener(keysChangeListener);
        allCharactersCountMap.addListener(charactersChangeListenerList);
        allWordsCountMap.addListener(wordsChangeListenerList);
    }

    public void put(File file) {
        FileEntity fileEntity = new FileEntity(
                file.getAbsolutePath(),
                AvailableFileExtensions.extensionType(file.getName()),
                charactersChangeListener,
                wordsChangeListener
        );
        files.put(file.getPath(), fileEntity);
    }

    public void clear() {
        allWordsCountMap.clear();
        allCharactersCountMap.clear();
        files.clear();
    }

    public void sort() {
        FXCollections.sort(allWordsCountList, currentlySelectedComparator);
        FXCollections.sort(allCharactersCountList, currentlySelectedComparator);
        files.values().forEach(file -> {
            FXCollections.sort(file.getCharactersCount(), currentlySelectedComparator);
            FXCollections.sort(file.getWordsCount(), currentlySelectedComparator);
        });
    }
}
