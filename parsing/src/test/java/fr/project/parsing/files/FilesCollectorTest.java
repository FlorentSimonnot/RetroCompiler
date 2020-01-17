package fr.project.parsing.files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class FilesCollectorTest {
    private final String path = "src/test/resources/JavaVersions/";
    private final int[] versions = {5, 6, 7, 8, 10, 11, 12, 13};

    @Test
    void shouldAddSimplyFileWithSuccess(){
        assertTrue(new FilesCollector().addFile(new FileClass(path+"j13.class")));
    }

    @Test
    void shouldAddSeveralFilesWithSuccess(){
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            assertTrue(collector.addFile(new FileClass(path+"j"+v+".class")));
        });
    }

    @Test
    void testGetSizeWithOneFileAdding(){
        var collector = new FilesCollector();
        collector.addFile(new FileClass(path+"j13.class"));
        assertEquals(1, collector.getSize());
    }

    @Test
    void testGetSizeWithManyFilesAdding(){
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            collector.addFile(new FileClass(path+"j"+v+".class"));
        });
        assertEquals(versions.length, collector.getSize());
    }

    @Test
    void testGetVersionsWithNoFileAdding(){
        assertEquals("No files in the collector", new FilesCollector().getVersions());
    }

    @Test
    void testGetVersionsWithOnFileAdding(){
        var collector = new FilesCollector();
        collector.addFile(new FileClass(path+"j13.class"));
        assertEquals(path+"j13.class - Java 13", collector.getVersions());
    }

    @Test
    void testGetVersionsWithManyFilesAdding(){
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            collector.addFile(new FileClass(path+"j"+v+".class"));
        });

        var joiner = new StringJoiner("\n");
        Arrays.stream(versions).forEach(v ->{
            joiner.add(path+"j"+v+".class"+" - Java "+v);
        });
        assertEquals(joiner.toString(), collector.getVersions());
    }

    @Test
    void testForEachOnACollector(){
        /* ADD FILES */
        var collector = new FilesCollector();
        Arrays.stream(versions).forEach(v -> {
            collector.addFile(new FileClass(path+"j"+v+".class"));
        });

        /* Compute the sum of versions in array */
        var joinerExpected = new StringJoiner("\n");
        Arrays.stream(versions).forEach(v ->{
            joinerExpected.add(v+"");
        });

        /* Compute the sum of versions in array */
        var joinerResult = new StringJoiner("\n");
        collector.forEach(v ->{
            try {
                joinerResult.add(v.getVersion()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        assertEquals(joinerExpected.toString(), joinerResult.toString());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenActionInForEachIsNull(){
        var collector = new FilesCollector();
        collector.addFile(new FileClass(path+"j13.class"));
        assertThrows(NullPointerException.class, () -> {
            collector.forEach(null);
        });
    }

    @Test
    void shouldReturnIsEmptyTrue(){
        assertTrue(new FilesCollector().isEmpty());
    }
}