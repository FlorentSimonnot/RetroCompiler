package fr.project.instructions.simple;

import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write an invoke dynamic instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class InvokeDynamicInstruction implements Instruction {
    private String name;
    private String descriptor;
    private Handle bootstrapMethodHandle;
    private Object[] bootstrapMethodArguments;

    /**
     * Creates a new InvokeDynamicInstruction.
     * @param name - the method's name
     * @param descriptor - the method's type
     * @param bootstrapMethodHandle - the bootstrap method
     * @param bootstrapMethodArguments - the bootstrap method constant arguments
     */
    public InvokeDynamicInstruction(String name, String descriptor, Handle bootstrapMethodHandle, Object[] bootstrapMethodArguments) {
        this.name = name;
        this.descriptor = descriptor;
        this.bootstrapMethodHandle = bootstrapMethodHandle;
        this.bootstrapMethodArguments = bootstrapMethodArguments;
    }

    @Override
    public String toString() {
        return "INDI INSN name " + name + " descriptor " + descriptor + " methodHandle " + bootstrapMethodHandle.getName();
    }

    /**
     * Writes the invoke dynamic instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }
}
