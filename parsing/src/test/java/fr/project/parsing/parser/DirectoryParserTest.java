package fr.project.parsing.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryParserTest {

    @Test
    void shouldFindEightFilesInDirectory() {
        var src = "src/test/resources/JavaVersions";
        assertAll(
                () -> {
                    assertEquals(8, new DirectoryParser().parseMyFile(src).getSize());
                }
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
            new DirectoryParser().parseMyFile(null);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIsNotDirectory(){
        assertThrows(IllegalArgumentException.class, () -> {
           new DirectoryParser().parseMyFile("j13.class");
        });
    }
}