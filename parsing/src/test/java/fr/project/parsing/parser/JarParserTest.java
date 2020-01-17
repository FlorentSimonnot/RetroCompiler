package fr.project.parsing.parser;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class JarParserTest {

    @Test
    void shouldGetSameSizeForJarAndDirectory(){
        var srcJar = "src/test/resources/JavaVersions.jar";
        var srcRental = "src/test/resources/JavaVersions";

        assertAll( () -> {
            assertEquals(new DirectoryParser().parseMyFile(srcRental).getSize(), new JarParser().parseMyFile(srcJar).getSize());
        });
    }

    @Test
    void shouldThrowNullPointerExceptionWhenPathNameIsNull(){
        assertThrows(NullPointerException.class, () -> {
           new JarParser().parseMyFile(null);
        });
    }

    @Test
    void shouldThrowParserExceptionWhenJarFileDoesntExists(){
        assertThrows(IllegalArgumentException.class, () -> {
           new JarParser().parseMyFile("");
        });
    }

    @Test
    void shouldThrowParserExceptionWhenPathNameIsNotAJarFile(){
        assertThrows(IllegalArgumentException.class, () -> {
            new JarParser().parseMyFile("./main/src/tests/resources/j13.class");
        });
    }
}