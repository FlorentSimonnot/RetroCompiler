package fr.project.parsing.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 
 * A class that describes a directory that contains the .class files.
 * This class is used when you run the project Retro with a directory as argument.
 * @author SIMONNOT Florent
 *
 */
public class DirectoryFile implements FileInterface {
    private final String name;
    private final String path;

    /**
     * Creates a new DirectoryFile.
     * @param name - the directory's name
     * @param path - the path of the directory
     */
    public DirectoryFile(String name, String path){
        this.name = name;
        this.path = path;
    }

    /**
     * Gets the full name of the directory.
     */
    @Override
    public String getName() {
        return path+name;
    }

    /**
     * Gets the java version of the DirectoryFile.
     */
    @Override
    public int getVersion() {
        return getClassReader().readByte(7)-44;
    }

    /**
     * Gets the full path of the directory.
     */
    @Override
    public String getPath() {
        return  path;
    }

    /**
     * Gets a ClassReader of the directory's name.
     */
    public ClassReader getClassReader(){
        try {
            return new ClassReader(new FileInputStream(path));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name + " in " + path);
        }
    }
}
