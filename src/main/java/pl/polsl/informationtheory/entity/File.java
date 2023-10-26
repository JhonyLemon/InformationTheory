package pl.polsl.informationtheory.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class File {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "PATH")
    private String path;

    @Column(name = "HASH")
    private String hash;

    @OneToMany(mappedBy = "file")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Letter> letters;

    @OneToMany(mappedBy = "file")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Word> words;
}
