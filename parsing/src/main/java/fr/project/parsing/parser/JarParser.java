package fr.project.parsing.parser;

import java.io.IOException;
import java.util.Objects;
import java.util.jar.JarFile;

import fr.project.parsing.files.FilesCollector;
import fr.project.parsing.files.JarFileC;

/**
 * 
 * A class that allows to parse a .jar file.
 * @author SIMONNOT Florent
 *
 */
public class JarParser implements FileParserInterface {

    @Override
    /**
     * Collects all .class files from a .jar file.
     */
    public FilesCollector parseMyFile(String name) throws IOException {
        return listFilesForFolder(Objects.requireNonNull(name));
    }

    private FilesCollector listFilesForFolder(String name) throws IOException {
        if(!name.endsWith(".jar"))
            throw new IllegalArgumentException();
        var collector = new FilesCollector();
        var jar = new JarFile(Objects.requireNonNull(name));
        var entries = jar.entries();
        var iterator = entries.asIterator();
        while(iterator.hasNext()) {
        	var f = iterator.next();
        	if (f.getRealName().endsWith(".class")) {
        		collector.addFile(new JarFileC(f.getRealName(), f.getName(), name));
        	}
        }
        jar.close();
        return collector;
    }
}
