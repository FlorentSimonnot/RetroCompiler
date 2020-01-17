package fr.project.parsing.files;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 
 * A class that describes a .class file.
 * This class is used when you run the project Retro with a simple .class file as argument.
 * @author SIMONNOT Florent
 *
 */
public class FileClass implements FileInterface{
    private final String name;

    /**
     * Creates a new FileClass.
     * @param name - the name of the .class file.
     */
    public FileClass(String name){
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Gets the name of the .class file.
     */
    public String getName(){return name;}

    /**
     * Gets the java version of the .class file.
     */
    @Override
    public int getVersion(){
        return getClassReader().readByte(7)-44;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the full path of the .class file.
     */
    @Override
    public String getPath() {
        var split = name.split("/");
        var sb = new StringBuilder("");
        Arrays.stream(split).forEach(s -> {
            if(!s.contains(".class")){
                sb.append(s).append("/");
            }
        });
        return sb.toString();
    }

    /**
     * Gets the ClassReader of the .class file.
     */
    public ClassReader getClassReader(){
        try {
            return new ClassReader(new FileInputStream(name));
        } catch (IOException e) {
            throw new IllegalStateException("Can't read the file " + name);
        }
    }
}
