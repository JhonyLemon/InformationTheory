package pl.polsl.informationtheory.entity;

import lombok.*;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;

import java.io.File;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class FileInfo {
    private final String path;
    private final AvailableFileExtensions extension;

    public FileInfo(String path, AvailableFileExtensions extension) {
        this.path = path;
        this.extension = extension;
    }

    public File getFile() {
        return new File(path);
    }
}
