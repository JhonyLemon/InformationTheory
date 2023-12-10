package pl.polsl.informationtheory.service.compression.algorithm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {
    private final int offset;
    private final int length;
    private byte literal;

    public Token(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }
}
