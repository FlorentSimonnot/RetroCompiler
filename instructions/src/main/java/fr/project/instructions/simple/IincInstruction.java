package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write a increment instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class IincInstruction implements Instruction {
	private final int var;
	private final int increment;
	
	/**
	 * Creates a new IincInstruction.
	 * @param var - index of the local variable to be incremented
	 * @param increment - amount to increment the local variable by
	 */
	public IincInstruction(int var, int increment) {
		this.var = var;
		this.increment = increment;
	}
	
	/**
	 * Writes the increment instruction into a .class file.
	 */
	@Override
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
		mv.visitIincInsn(var, increment);
	}
	
	@Override
	public String toString() {
		return "IINC INSN var: " + var + " increment: " + increment;
	}
	
}
