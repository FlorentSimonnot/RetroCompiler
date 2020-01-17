package fr.project.instructions.features;

import fr.project.instructions.simple.Instruction;
import fr.project.instructions.simple.InstructionsCollector;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * A class that allows to write a try-with-resources in bytecode into different versions.
 * It is stored as an Instruction of a Method. And this Method object is used into a MethodVisitor also used into a ClassVisitor.
 * The ClassVisitor will visit a .class file and write a code block in bytecode corresponding to a try-with-resources from the .class file on a new .class file according to the version required.
 * @author SIMONNOT Florent
 *
 */

public class TryWithResourcesInstruction implements Instruction {
    private static final int VERSION = Opcodes.V1_7;
    private final InstructionsCollector instructions;

    /**
     * Creates a new TryWithResourcesInstruction.
     * @param instructions - the instructions block attached to this try-with-resources instruction
     */
    public TryWithResourcesInstruction(InstructionsCollector instructions) {
        this.instructions = instructions;
    }

    /**
     * Writes the bytecode corresponding to the try-with-resources instruction according to the version given.
     * @param version - the target version
     * @param mv - the MethodVisitor object attached to the .class file
     * @param lastInstruction - the last instruction writen
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {

    }

    /**
     * Returns if this instruction is an aload instruction.
     * @return false
     */
    @Override
    public boolean isAloadInstruction() {
        return false;
    }
}
