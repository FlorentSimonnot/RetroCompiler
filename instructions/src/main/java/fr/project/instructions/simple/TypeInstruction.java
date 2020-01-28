package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

/**
 * 
 * A class that allows to detect and write a type instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class TypeInstruction implements Instruction {
    private final int opcode;
    private final String owner;

    /**
     * Creates a new TypeInstruction.
     * @param opcode - the opcode of the type instruction to be visited
     * @param owner - the operand of the instruction to be visited
     */
    public TypeInstruction(int opcode, String owner) {
        if(opcode < 0) throw  new IllegalArgumentException("Opcode must be positive");
        this.opcode = opcode;
        this.owner = Objects.requireNonNull(owner);
    }

    @Override
    public String toString() {
        return "TYPE INSN " + opcode + " owner : " + owner;
    }

    /**
     * Tests if the type instruction is an load instruction.
     */
    @Override
    public boolean isLoadInstruction() {
        return opcode == Opcodes.ALOAD || opcode == Opcodes.ILOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD;
    }

    /**
     * Writes the type instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        mv.visitTypeInsn(opcode, owner);
    }

    /**
     * Tests if the type instruction is a new instruction.
     */
    @Override
    public boolean isNew() {
        return opcode == Opcodes.NEW;
    }
}
