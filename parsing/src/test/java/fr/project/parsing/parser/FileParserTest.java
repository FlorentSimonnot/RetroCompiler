package fr.project.parsing.parser;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {
    private final String path = "src/test/resources/";

    @Test
    void shouldReturnADirectoryWhenADirectoryIsPassedEvenIfHisNameIsJar(){
        assertAll(
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get(path + "test.jar")) instanceof DirectoryParser);
                },
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get(path + "test.class")) instanceof DirectoryParser);
                }
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNotSupported(){
        assertAll(
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> ParserFactory.createParser(Paths.get(path + "rental.zip")));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> ParserFactory.createParser(Paths.get(path + "lol.docx")));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> ParserFactory.createParser(Paths.get(path + "noAClassFile.txt")));
                }
        );
    }
}