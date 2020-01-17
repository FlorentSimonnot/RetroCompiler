package fr.project.instructions.features;

import fr.project.instructions.simple.Instruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

/**
 * A class that allows to write a called lambda instruction in bytecode into different versions.
 * It is stored as an Instruction of a Method. And this Method object is used into a MethodVisitor also used into a ClassVisitor.
 * The ClassVisitor will visit a .class file and write a code block in bytecode corresponding to a called lambda from the .class file on a new .class file according to the version required.
 * @author SIMONNOT Florent
 *
 */
public class CalledLambdaInstruction implements Instruction {
    private final LambdaInstruction lambdaCalled;
    /* Save the descriptor for write early*/
    private final String descriptor;
    private final int index;
    private final String className;

    /**
     * Creates a new CalledLambdaInstruction.
     * @param lambdaCalled - a LambdaInstruction object
     * @param descriptor - the lambda's descriptor
     * @param index - the lambda's index
     * @param className - the lamda's name
     */
    public CalledLambdaInstruction(LambdaInstruction lambdaCalled, String descriptor, int index, String className){
        this.lambdaCalled = Objects.requireNonNull(lambdaCalled);
        this.descriptor = Objects.requireNonNull(descriptor);
        this.index = index;
        this.className = className;
    }

    /**
     * Writes the bytecode corresponding to the called lambda instruction according to the version given.
     * @param version - the target version
     * @param mv - the MethodVisitor object attached to the .class file
     * @param lastInstruction - the last Instruction writen
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        if(version < lambdaCalled.getVersion()){
            writeOldVersion(mv, lastInstruction, className);
            return;
        }
        writeNewVersion(mv, lastInstruction, className);
    }

    private void writeOldVersion(MethodVisitor mv, Instruction lastInstruction, String className){
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className+"$MyLambda"+index, "myLambdaFunction$"+index, descriptor, false);
        //mv.visitVarInsn(Opcodes.ISTORE, 0);
    }

    private void writeNewVersion(MethodVisitor mv, Instruction lastInstruction, String className){
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, lambdaCalled.getOwnerClass(), lambdaCalled.getName(), descriptor, true);
    }
}
