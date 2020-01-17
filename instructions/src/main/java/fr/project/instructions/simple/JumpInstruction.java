package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * A class that allows to detect and write a jump instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class JumpInstruction implements Instruction {
    private final int opcode;
    private final Label label;

    /**
     * Creates a new JumpInstruction.
     * @param opcode - the opcode of the type instruction to be visited 
     * @param label - the operand of the instruction to be visited
     */
    public JumpInstruction(int opcode, Label label) {
        this.opcode = opcode;
        this.label = label;
    }

    @Override
    public String toString() {
        if(opcode == Opcodes.GOTO)
            return "GOTO INSN " + label;
        return "JUMP INSN " + label;
    }

    /**
     * Writes the jump instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        mv.visitJumpInsn(opcode, label);
    }
}
