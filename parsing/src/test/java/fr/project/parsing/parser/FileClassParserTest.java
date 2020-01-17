package fr.project.parsing.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileClassParserTest {
    private final String path = "src/test/resources/JavaVersions/";

    @Test
    void shouldReturnOneFile(){
        assertAll (
                () -> assertEquals(1, new FileClassParser().parseMyFile(path+"j13.class").getSize())
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenFileIsNull(){
        assertThrows(NullPointerException.class, () -> {
           new FileClassParser().parseMyFile(null);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNotAClassFile(){
        assertThrows(IllegalArgumentException.class, () -> {
            new FileClassParser().parseMyFile("src/tests/resources/notAClassFile.txt");
        });
    }
}