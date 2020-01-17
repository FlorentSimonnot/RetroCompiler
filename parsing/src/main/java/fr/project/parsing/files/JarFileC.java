package fr.project.parsing.files;

import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.objectweb.asm.ClassReader;

/**
 * 
 * A class that describes a .jar file.
 * This class is used when you run the project Retro with a .jar file as argument.
 * @author SIMONNOT Florent
 *
 */
public class JarFileC implements FileInterface {
    private final String name;
    private final String entry;
    private final String zipName;

    /**
     * Creates a new JarFileC.
     * @param name - the name of the .class file from the .jar file
     * @param entry - the name of a jar's entry
     * @param zipName - the name of .jar file
     */
    public JarFileC(String name, String entry, String zipName){
        this.name = name;
        this.entry = entry;
        this.zipName = zipName;
    }

    /**
     * Gets the name of the .jar file.
     */
    @Override
    public String getName() {
        return zipName+"/"+entry+"/"+name;
    }

    /**
     * Gets the java version of the .jar file.
     */
    @Override
    public int getVersion() throws IOException{
        return getClassReader().readByte(7)-44;
    }

    /**
     * Gets the full path of the .jar file.
     */
    @Override
    public String getPath() {
        return zipName;
    }

    /**
     * Gets the ClassReader of the .jar file.
     * @throws IOException - if the JarFile cannot be opened
     */
    public ClassReader getClassReader() throws IOException{
        try (var jar = new JarFile(zipName)) {
        	var inputStream = jar.getInputStream(new ZipEntry(entry));
        	return new ClassReader(inputStream);
        }
    }
}
