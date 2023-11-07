package pl.polsl.informationtheory.service;

import javafx.beans.value.ObservableValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.informationtheory.entity.Data;
import pl.polsl.informationtheory.repository.MenuOptionsRepository;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class MenuOptionsService {

    private final MenuOptionsRepository menuOptionsRepository;

    public ObservableValue<Boolean> useUnicode() {
        return menuOptionsRepository.useUnicode();
    }

    public ObservableValue<Comparator<Data>> getComparator() {
        return menuOptionsRepository.getCurrentlySelectedComparator();
    }

}
