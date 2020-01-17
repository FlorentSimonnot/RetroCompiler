package fr.project.parsing.parser;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ParserFactoryTest {
    private final String path = "src/test/resources/";

    @Test
    void shouldThrowNullPointerExceptionWhenPathIsNull(){
        assertAll(
                () -> {
                    assertThrows(NullPointerException.class, () -> {
                        ParserFactory.createParser(Paths.get(null));
                    });
                }
        );
    }

    @Test
    void shouldCreateAFileParserForAClassFile(){
        assertAll(
            () -> {
                assertTrue(ParserFactory.createParser(Paths.get(path+"JavaVersions/j13.class")) instanceof FileClassParser);
            }
        );
    }

    @Test
    void shouldCreateADirectoryParserForADirectory(){
        assertAll(
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get(path+"JavaVersions")) instanceof DirectoryParser);
                }
        );
    }

    @Test
    void shouldCreateAJarParserForAJarFile(){
        assertAll(
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get(path+"testJar.jar")) instanceof JarParser);
                }
        );
    }

    @Test
    void shouldThrowExceptionWhenFileIsNotAClassFile(){
        assertAll(
                () -> {
                    assertThrows(IllegalArgumentException.class,
                            () -> ParserFactory.createParser(Paths.get(path+"notAClassFile.txt")));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class,
                            () -> ParserFactory.createParser(Paths.get(path+"lol.docx")));
                }
        );
    }

    @Test
    void shouldCreateADirectoryParserEvenIfDirectoryContainDotClass(){
        assertAll(
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get(path+"test.class")) instanceof DirectoryParser);
                }
        );
    }

    @Test
    void shouldCreateADirectoryEvenIfDirectoryContainDotJar(){
        assertAll(
                () -> {
                    assertTrue(ParserFactory.createParser(Paths.get(path+"test.jar")) instanceof DirectoryParser);
                }
        );
    }
}