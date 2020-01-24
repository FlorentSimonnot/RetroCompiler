package fr.project.instructions.features;

import fr.project.instructions.simple.Instruction;
import fr.project.instructions.simple.Utils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

/**
 * A class that allows to write an instantiate lambda instruction in bytecode into different versions.
 * It is stored as an Instruction of a Method. And this Method object is used into a MethodVisitor also used into a ClassVisitor.
 * The ClassVisitor will visit a .class file and write a code block in bytecode corresponding to an instantiate lambda from the .class file on a new .class file according to the version required.
 * @author SIMONNOT Florent
 *
 */
public class InstantiateLambdaInstruction implements Instruction{
    private final LambdaInstruction lambda;
    private final int index;
    private final String className;

    /**
     * Creates a new InstantiateLambdaInstruction.
     * @param lambda - a LambdaInstruction object
     * @param index - the lambda's index
     * @param className - the lamda's name
     */
    public InstantiateLambdaInstruction(LambdaInstruction lambda, int index, String className){
        if(index < 0) throw new IllegalArgumentException("Index must be positive");
        this.lambda = Objects.requireNonNull(lambda);
        this.index = index;
        this.className = className;
    }

    /**
     * Writes the bytecode corresponding to the instantiate lambda instruction according to the version given.
     * @param version - the target version
     * @param mv - the MethodVisitor object attached to the .class file
     * @param lastInstruction - the last Instruction writen
     */
    @Override
    public void  writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        if(version < lambda.getVersion()){
            writeOldVersion(mv, lastInstruction);
            return;
        }
        writeNewVersion(mv, lastInstruction);
    }

    private void writeOldVersion(MethodVisitor mv, Instruction lastInstruction){
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, className+"$MyLambda"+index, "myLambdaFactory$"+index, "("+Utils.takeCapture(lambda.getDescriptor())+")Linterface"+className+"$MyLambda"+index+";", false);
    }


    private void writeNewVersion(MethodVisitor mv, Instruction lastInstruction){
        mv.visitInvokeDynamicInsn(lambda.getName(), lambda.getDescriptor(), lambda.getMethodHandle(), lambda.getBootstrapMethodArguments());
    }
}
