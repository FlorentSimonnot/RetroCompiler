package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.Field;
import fr.project.instructions.simple.InnerClass;
import fr.project.instructions.simple.Method;
import fr.project.instructions.simple.MyClass;
import fr.project.warningObservers.WarningObserver;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public interface Writer {

    public void createClass();

    /**
     * Writes all the lambdas of a .class file into a new .class file according to a target version.
     */
    public void writeLambdaInnerClasses();

    /**
     * Create and write a .class file for each lambda which are founded.
     */
    public void writeLambdaFiles();

    /**
     * Writes all fields of the current class.
     */
    public void writeFields();

    /**
     * Writes all constructors of the current class.
     */
    public void writeConstructors();

    /**
     * Writes all methods of the current class.
     */
    public void writeMethods();

    /**
     * Creates a new .class file with the bytecode.
     * @return the path of the file which contain the bytecode
     * @throws IOException - if problem occurred during the creating or writing of the file.
     */
    public String createFile() throws IOException;

    /**
     * return a list of observers which represents warnings.
     * @return List<WarningObserver> - a List of WarningObserver.
     */
    public List<WarningObserver> getObservers();

    /**
     * Return the class we want write.
     * @return MyClass - an object which represents a class.
     */
    MyClass getMyClass();

    /**
     * Return the method visitor use to write a method.
     * @return MethodVisitor - A MethodVisitor to write a method.
     */
    MethodVisitor getMethodVisitor();

    void setMethodVisitor(MethodVisitor methodVisitor);

    /**
     * Return the java version use to write the file.
     * @return int - The java version.
     */
    int getVersion();


    ClassWriter getClassWriter();
}
