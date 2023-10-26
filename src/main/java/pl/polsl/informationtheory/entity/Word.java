package pl.polsl.informationtheory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Word {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "WORD")
    private String word;

    @Column(name = "COUNT")
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "FILE_ID", referencedColumnName = "ID", nullable = false)
    private File file;
}
