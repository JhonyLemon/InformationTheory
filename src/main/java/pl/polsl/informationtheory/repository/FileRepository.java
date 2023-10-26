package pl.polsl.informationtheory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.informationtheory.entity.File;

public interface FileRepository extends JpaRepository<File, Integer> {
}
