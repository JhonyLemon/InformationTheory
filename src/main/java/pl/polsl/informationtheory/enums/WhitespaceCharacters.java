package pl.polsl.informationtheory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum WhitespaceCharacters {
    SPACE(' ', "U+0020",  "SPACE"),
    SPACE_SEPARATOR('\u00A0', "U+00A0",  "SPACE_SEPARATOR"),
    LINE_SEPARATOR('\u2007', "U+2007", "LINE_SEPARATOR"),
    PARAGRAPH_SEPARATOR('\u202F', "U+202F", "PARAGRAPH_SEPARATOR"),
    HORIZONTAL_TABULATION('\t', "U+00A0", "HORIZONTAL_TABULATION"),
    LINE_FEED('\n', "U+00A0", "LINE_FEED"),
    VERTICAL_TABULATION('\u000B', "U+000B", "VERTICAL_TABULATION"),
    FORM_FEED('\f', "U+00A0", "FORM_FEED"),
    CARRIAGE_RETURN('\r', "U+00A0", "CARRIAGE_RETURN"),
    FILE_SEPARATOR('\u001C', "U+001C", "FILE_SEPARATOR"),
    GROUP_SEPARATOR('\u001D', "U+001D", "GROUP_SEPARATOR"),
    RECORD_SEPARATOR('\u001E', "U+001E", "RECORD_SEPARATOR"),
    UNIT_SEPARATOR('\u001F', "U+001F", "UNIT_SEPARATOR");

    private final Character unicode;
    private final String escapedWhitespaceCharacter;
    private final String label;

    public static WhitespaceCharacters findFromUniCode(Character unicode) {
        return Arrays.stream(WhitespaceCharacters.values())
                .filter(wc -> wc.getUnicode().equals(unicode))
                .findFirst()
                .orElse(null);
    }
}


