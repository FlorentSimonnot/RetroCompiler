package fr.project.writer;

import fr.project.instructions.simple.Method;
import fr.project.instructions.simple.MyInterface;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Simple class which represents a writer.
 * It allows to write an interface.
 */
public class InterfaceWriter {
    private final MyInterface myInterface;
    private final ClassWriter cw;

    public InterfaceWriter(MyInterface myInterface) {
        this.myInterface = myInterface;
        this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    }

    /**
     * public void createClass()
     * Declares the structure of the class in the class writer.
     */
    public void createClass(){
        cw.visit(myInterface.getVersion(), myInterface.getPrivacy(), myInterface.getClassName(), null, "java/lang/Object", null);
    }

    /**
     * Writes all methods of the current class.
     */
    public void writeMethods(){
        myInterface.getMethods().forEach(this::writeMethod);
    }

    private void writeMethod(Method m) {
        cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), m.getSignature(), m.getExceptions());
    }

    /**
     * Creates a new .class file with the bytecode.
     * @return the path of the file which contain the bytecode
     * @throws IOException - if problem occurred during the creating or writing of the file.
     */
    public String createFile() throws IOException{
        FileOutputStream fos = new FileOutputStream(myInterface.getClassName()+".class");
        fos.write(cw.toByteArray());
        fos.close();
        return myInterface.getClassName()+".class";
    }

}
