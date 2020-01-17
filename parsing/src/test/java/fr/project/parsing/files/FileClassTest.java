package fr.project.parsing.files;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileClassTest {
    private final String path = "src/test/resources/JavaVersions/";

    @Test
    void shouldThrowNullPointerExceptionWhenNameFileIsNull(){
        assertThrows(NullPointerException.class, () -> {
           new FileClass(null);
        });
    }

    @Test
    void testToStringMethod(){
        assertEquals(path+"j13.class", new FileClass(path+"j13.class").toString());
    }

    @Test
    void testGetNameMethod(){
        assertEquals(path+"j13.class", new FileClass(path+"j13.class").getName());
    }

    @Test
    void testGetVersionMethod(){
        assertEquals(13, new FileClass(path+"j13.class").getVersion());
    }

}