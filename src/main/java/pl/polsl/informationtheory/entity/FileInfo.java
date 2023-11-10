package pl.polsl.informationtheory.entity;

import lombok.*;
import pl.polsl.informationtheory.enums.AvailableFileExtensions;

import java.io.File;

@Getter
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

    @Override
    public String toString() {
        return path;
    }
}
