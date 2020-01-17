package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write a ldc instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class LdcInstruction implements Instruction {
    private final Object value;

    /**
     * Creates a new LdcInstruction.
     * @param value - the constant to be loaded on the stack
     */
    public LdcInstruction(Object value) {
        this.value = value;
    }

    /**
     * Writes the ldc instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        mv.visitLdcInsn(value);
    }

    @Override
    public String toString() {
        return "LDC : " + value;
    }
}
