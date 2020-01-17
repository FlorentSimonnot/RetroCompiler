package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write a label instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class LabelInstruction implements Instruction {
    private final Label label;

    /**
     * Creates a new LabelInstruction.
     * @param label - a Label object
     */
    public LabelInstruction(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "LABEL INSN " + label;
    }

    /**
     * Writes the label instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        mv.visitLabel(label);
    }
}
