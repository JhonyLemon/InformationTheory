package pl.polsl.informationtheory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.informationtheory.entity.Word;

public interface WordRepository extends JpaRepository<Word, Integer> {
}
