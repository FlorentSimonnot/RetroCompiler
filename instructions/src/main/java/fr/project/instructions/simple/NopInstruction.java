package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * A class that allows to detect and write a zero instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class NopInstruction implements Instruction {
    private final int opcode;

    /**
     * Creates a new NopInstruction.
     * @param opcode - the opcode of the instruction to be visited
     */
    public NopInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return "NOP INSN " + opcode;
    }

    /**
     * Tests if the zero instruction is an aload instruction.
     */
    @Override
    public boolean isAloadInstruction() {
        return opcode == Opcodes.ALOAD;
    }

    /**
     * Writes the zero instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        mv.visitInsn(opcode);
    }

}
