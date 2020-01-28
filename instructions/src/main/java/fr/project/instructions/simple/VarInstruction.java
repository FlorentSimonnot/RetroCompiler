package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;
import java.util.Optional;

/**
 * 
 * A class that allows to detect and write a variable instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class VarInstruction implements Instruction {
    private final int var;
    private final int opcode;

    /**
     * Creates a new VarInstruction.
     * @param opcode - the opcode of the local variable instruction to be visited
     * @param var - the operand of the instruction to be visited
     */
    public VarInstruction(int opcode, int var) {
        if(opcode < 0 || var < 0) throw new IllegalArgumentException("Opcode and Var must be positives");
        this.opcode = opcode;
        this.var = var;
    }

    /**
     * Gets the type of the variable instruction.
     */
    @Override
    public String getType() {
        switch(opcode){
            case Opcodes.ILOAD : return "(I)";
            case Opcodes.LLOAD: return "(J)";
            case Opcodes.DLOAD: return "(D)";
            case Opcodes.ALOAD : return "(Ljava/lang/Object;)";
            default : return "()";
        }
    }

    @Override
    public String toString() {
        return "VAR INSN " + opcode + " name : " + var;
    }

    /**
     * Tests if the variable instruction is an load instruction.
     */
    @Override
    public boolean isLoadInstruction() {
        return opcode == Opcodes.ALOAD || opcode == Opcodes.ILOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD;
    }

    public Optional<Integer> getVar(){
        return Optional.of(var);
    }

    /**
     * Writes the variable instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        Objects.requireNonNull(mv);
        mv.visitVarInsn(opcode, var);
    }
}
