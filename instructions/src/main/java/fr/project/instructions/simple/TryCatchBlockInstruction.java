package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * A class that allows to detect and write a try catch block instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class TryCatchBlockInstruction implements Instruction {
	private final Label start;
	private final Label end;
	private final Label handler;
	private final String type;
	
	/**
	 * Creates a new TryCatchBlockInstruction.
	 * @param start - the beginning of the exception handler's scope
	 * @param end - the end of the exception handler's scope
	 * @param handler - the beginning of the exception handler's code
	 * @param type - the internal name of the type of exceptions handled by the handler
	 */
	public TryCatchBlockInstruction(Label start, Label end, Label handler, String type) {
		this.start = start;
		this.end = end;
		this.handler = handler;
		this.type = type;
	}

	/**
	 * Gets the beginning of the try catch block instruction.
	 * @return the Label that represents the beginning of the try catch block instruction
	 */
	public Label getStart() {
		return start;
	}
	
	/**
	 * Gets the end of the try catch block instruction.
	 * @return the Label that represents the end of the try catch block instruction
	 */
	public Label getEnd() {
		return end;
	}

	/**
	 * Writes the try catch block instruction into a .class file.
	 */
	@Override
	public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
		mv.visitTryCatchBlock(start, end, handler, type);
	}
	
	@Override
	public String toString() {
		return "TRY CATCH BLOCK INSN " + type ;
	}

}
