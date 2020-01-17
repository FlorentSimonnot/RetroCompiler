package fr.project.detection.visitor;

import fr.project.detection.classVisitor.MyClassVisitor;
import fr.project.detection.observers.FeatureObserver;
import fr.project.parsing.files.FileInterface;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.IOException;

/**
 * 
 * A visitor to visit a .class file.
 * It can detect the features asked by the user of the project Retro and write them if the user wants it.
 * @author CHU Jonathan
 *
 */
public class MyVisitor {
    private final FileInterface file;
    private final ClassWriter writer;
    private final MyClassVisitor visitor;
    private final List<FeatureObserver> observers = new ArrayList<>();

    /**
     * Creates a new MyVisitor
     * @param file - the file you want to visit
     * @param observers - a list of FeatureObserver you want to link with the MyClassVisitor
     */
    public MyVisitor(FileInterface file, List<FeatureObserver> observers){
        this.file = Objects.requireNonNull(file);
        this.writer = new ClassWriter(0);
        this.visitor = new MyClassVisitor(Opcodes.ASM7, writer, Objects.requireNonNull(observers));
    }

    /**
     * Adds all the FeaturesObserver into the field observers.
     * @param observers - a list of FeatureObservers you want to link
     * @return true if all FeatureObserver are added successfully, false if not
     */
    public boolean addAll(List<FeatureObserver> observers){
        return addAll(observers);
    }

    /**
     * Gets the ClassReader attached to the .class file.
     * @return the ClassReader attached to the file.
     * @throws IOException - if the file cannot be opened
     */
    public ClassReader getClassReader() throws IOException {
        return file.getClassReader();
    }

    /**
     * Gets the ClassWriter attached to the object MyVisitor.
     * @return the ClassWriter attached to the object MyVisitor.
     */
    public ClassWriter getClassWriter(){
        return writer;
    }

    /**
     * Gets the MyClassVisitor attached to the object MyVisitor.
     * @return the MyClassVisitor attached to the object MyVisitor.
     */
    public MyClassVisitor getClassVisitor(){
        return visitor;
    }

    /**
     * Gets the list of FeatureObserver attached to the object MyVisitor.
     * @return the list of FeatureObserver attached to the object MyVisitor.
     */
    public List<FeatureObserver> getObservers(){
        return observers;
    }
}
