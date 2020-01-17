package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

import java.util.*;

/**
 * 
 * A class that represents a method of a .class file.
 * It contains method that allow to detect and write all instructions of the method.
 * @author CHU Jonathan
 *
 */
public class Method {
	private final int access;
	private final String name;
	private final String descriptor;
	private final String signature;
	private final boolean isInterface;
	private final String[] exceptions;
	private final InstructionsCollector instructions;

	/**
	 * Creates a new Method.
	 * @param access - the method's access flags
	 * @param name the method's name
	 * @param descriptor - the method's descriptor
	 * @param signature - the method's signature
	 * @param isInterface - if the method's owner class is an interface
	 * @param exceptions - the exceptions thrown by the method
	 */
	public Method(int access, String name, String descriptor, String signature, boolean isInterface, String[] exceptions) {
		this.name = name;
		this.signature = signature;
		this.descriptor = descriptor;
		this.access = access;
		this.isInterface = isInterface;
		this.exceptions = exceptions;
		this.instructions = new InstructionsCollector();
	}

	/* ********************************** *\
                    GETTERS
    \* ********************************** */

	/**
	 * Gets the method's name.
	 * @return the method's name
	 */
	public String getName(){return name;}

	/**
	 * Gets the method's access flags
	 * @return the method's access flags
	 */
	public int getAccess(){return access;}

	/**
	 * Gets the method's descriptor.
	 * @return the method's descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * Gets the method's signature.
	 * @return the method's signature
	 */
	public String getSignature(){return signature;}

	/**
	 * Gets the method's exceptions.
	 * @return the method's exceptions
	 */
	public String[] getExceptions(){return exceptions;}

	/**
	 * Gets the method's last instruction.
	 * @return the method's last instruction
	 */
	public Instruction getLastInstruction(){return instructions.getInstruction(instructions.size()-1);}

	/* ********************************** *\
                    OTHERS
    \* ********************************** */
	/**
	 * Adds an instruction into the field instructions.
	 * @param instruction - an Instruction object
	 */
	public void addInstruction(Instruction instruction){
		instructions.add(instruction);
	}

	/**
	 * Writes all instructions of the Method object into a .class file.
	 * @param version - the target version of the Method object
	 * @param mv - the MethodVisitor attached to this Method
	 */
	public void writeAllInstructions(int version, MethodVisitor mv, String className){
		instructions.writeAllInstruction(version, mv, className);
	}

	/**
	 * Sets the field instructions with a new concatenation of all Instruction.
	 * @param nArgs - a number of arguments
	 * @param format - the text format
	 */
	public void createConcatenationInstruction(int nArgs, List<String> format){
		var map = instructions.createConcatenationInstruction(nArgs, format);
		instructions.clear();
		instructions.addAll(map);
	}

	/**
	 * Tests if the field instructions is empty.
	 * @return true if the field instructions is empty, false if not
	 */
	public boolean collectorIsEmpty(){return instructions.size() == 0;}

	/* ********************************** *\
                    DEBUG
    \* ********************************** */
	/**
	 * Displays the Method object and all its instructions.
	 */
	public void printInstructions(){
		System.out.println("-----------------------METHOD " + name+"----------------------\n");
		instructions.forEach(i -> System.out.println(i.toString()));
		System.out.println("-----------------------END--------------------------\n");
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Method)) return false;
		Method method = (Method) o;
		return getAccess() == method.getAccess() &&
				isInterface == method.isInterface &&
				Objects.equals(getName(), method.getName()) &&
				Objects.equals(getDescriptor(), method.getDescriptor()) &&
				Objects.equals(signature, method.signature) &&
				Arrays.equals(exceptions, method.exceptions);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(getAccess(), getName(), getDescriptor(), signature, isInterface);
		result = 31 * result + Arrays.hashCode(exceptions);
		return result;
	}
}
