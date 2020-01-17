package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write a line number instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class LineNumberInstruction implements Instruction {
    private final int lineNumber;
    private final Label start;

    /**
     * Creates a new LineNumberInstruction.
     * @param lineNumber - a line number
     * @param start - the first instruction corresponding to this line number
     */
    public LineNumberInstruction(int lineNumber, Label start) {
        this.lineNumber = lineNumber;
        this.start = start;
    }

    @Override
    public String toString() {
        return "VISIT LINE " + lineNumber + " BEGIN LABEL " + start;
    }

    /**
     * Writes the line number instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        mv.visitLineNumber(lineNumber, start);
    }
}
