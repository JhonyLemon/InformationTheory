package pl.polsl.informationtheory.fxml.task;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.informationtheory.context.SpringContext;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.entity.FileData;
import pl.polsl.informationtheory.entity.SummedData;
import pl.polsl.informationtheory.enums.TaskType;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;

import java.util.*;
import java.util.stream.Stream;

import static pl.polsl.informationtheory.util.StreamHelper.valueEquals;

@Slf4j
@RequiredArgsConstructor
public class GenerateSummedDataTask extends Task<SummedData> {
    private static final TaskType TYPE = TaskType.SUMMING;

    private final Collection<FileData> data;
    private final TaskOnProgressDataChange onUpdate;

    @Override
    protected void updateProgress(double v, double v1) {
        Platform.runLater(() -> onUpdate.onProgressUpdate(v / v1, TYPE));
        super.updateProgress(v, v1);
    }

    @Override
    protected void updateMessage(String s) {
        Platform.runLater(() -> onUpdate.onMessagesUpdate(s, TYPE));
        super.updateMessage(s);
    }

    @Override
    protected void succeeded() {
        log.info("Task: {} succeeded", TYPE);
        Platform.runLater(() -> onUpdate.onFinish(true, TYPE));
        super.succeeded();
    }

    @Override
    protected void failed() {
        log.info("Task: {} failed", TYPE);
        Platform.runLater(() -> onUpdate.onFinish(false, TYPE));
        super.failed();
    }

    @Override
    protected SummedData call() {
        log.info("Generating summed file data");
        Platform.runLater(() -> onUpdate.onProgressUpdate(-1d, TYPE));
        Integer wordCount = data.stream().map(FileData::getWordsCount).mapToInt(Integer::intValue).sum();
        Integer charactersCount = data.stream().map(FileData::getCharactersCount).mapToInt(Integer::intValue).sum();

        Map<String, Data> words = Collections.synchronizedMap(new HashMap<>());
        Map<String, Data> characters = Collections.synchronizedMap(new HashMap<>());

        extract(charactersCount, characters, data.parallelStream().map(FileData::getCharacters));
        updateProgress(1d,6d);
        extract(wordCount, words, data.parallelStream().map(FileData::getWords));
        updateProgress(2d,6d);

        words.values().parallelStream().forEach(Data::initialize);
        updateProgress(3d,6d);
        characters.values().parallelStream().forEach(Data::initialize);
        updateProgress(4d,6d);

        return new SummedData(words.values(), characters.values());
    }

    private void extract(Integer count, Map<String, Data> values, Stream<List<Data>> stream) {
        stream.flatMap(List::stream).forEach(w -> {
            if (values.containsKey(w.getValue())) {
                values.get(w.getValue()).addCount(w.getCount());
            } else {
                Data d = new Data(w.getType(), w.getValue(), w.getCount());
                d.setCountAll(count);
                values.put(w.getValue(), d);
            }
        });
    }
}

