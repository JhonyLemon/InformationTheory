package pl.polsl.informationtheory.entity;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.*;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class FileEntity {

    private final String path;
    @ToString.Exclude
    private final AvailableFileExtensions extension;
    @ToString.Exclude
    private final ObservableMap<String, Integer> characters = new ObservableMapWrapper<>(new HashMap<>());
    @ToString.Exclude
    private final ObservableMap<String, Integer>  words = new ObservableMapWrapper<>(new HashMap<>());

    private final ObservableList<Map.Entry<String, Integer>> wordsCount = new ObservableListWrapper<>(new ArrayList<>());

    private final ObservableList<Map.Entry<String, Integer>> charactersCount = new ObservableListWrapper<>(new ArrayList<>());

    private final MapChangeListener<String, Integer> wordsChangeListener = (change) -> {
        if(change.wasAdded()) {
            wordsCount.add(Map.entry(change.getKey(), change.getValueAdded()));
        }
    };

    private final MapChangeListener<String, Integer> charactersChangeListener = (change) -> {
        if(change.wasAdded()) {
            charactersCount.add(Map.entry(change.getKey(), change.getValueAdded()));
        }
    };

    public FileEntity(String path, AvailableFileExtensions extension, MapChangeListener<String, Integer> charactersChangeListener, MapChangeListener<String, Integer> wordsChangeListener) {
        this.path = path;
        this.extension = extension;

        this.words.addListener(wordsChangeListener);
        this.words.addListener(this.wordsChangeListener);
        this.characters.addListener(charactersChangeListener);
        this.characters.addListener(this.charactersChangeListener);
    }

    public File getFile() {
        return new File(path);
    }
}
