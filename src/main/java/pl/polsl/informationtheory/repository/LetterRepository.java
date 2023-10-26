package pl.polsl.informationtheory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.informationtheory.entity.Letter;

public interface LetterRepository extends JpaRepository<Letter, Integer> {
}
