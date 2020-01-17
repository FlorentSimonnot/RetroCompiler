package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write a frame instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class FrameInstruction implements Instruction {
    private final int type;
    private final int numLocal;
    private final Object[] local;
    private final int numStack;
    private final Object[] stack;

    /**
     * Creates a new FrameInstruction.
     * @param type - the type of this stack map frame
     * @param numLocal - the number of local variables in the visited frame
     * @param local - the local variable types in this frame
     * @param numStack - the number of operand stack elements in the visited frame
     * @param stack - the operand stack types in this frame
     */
    public FrameInstruction(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.type = type;
        this.numLocal = numLocal;
        this.local = local;
        this.numStack = numStack;
        this.stack = stack;
    }

    @Override
    public String toString() {
        return "Frame Instruction " + type;
    }

    /**
     * Writes the frame instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        mv.visitFrame(type, numLocal, local, numStack, stack);
    }
}
