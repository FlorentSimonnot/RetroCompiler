package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;

import java.util.Optional;

/**
 * 
 * An interface that represents any type of instruction of a .class file.
 * @author CHU Jonathan
 *
 */
public interface Instruction {

	/**
	 * Writes the Instruction according to a version into a .class file.
	 * @param version - the target version of the Instruction
	 * @param mv - the MethodVisitor attached to a .class file
	 * @param lastInstruction -  the Instruction that precedes this Instruction
	 */
    void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className);

    /**
     * Adds an Instruction into a Method.
     * @param instruction - the instruction you want to add
     */
    default void addInstruction(Instruction instruction){/*Nothing*/}
    
    /**
     * Tests if an Instruction is a load instruction.
     * @return true if this Instruction is a load, false if not
     */
    default boolean isLoadInstruction(){return false;}
    
    /**
     * Tests if an Instruction is a record invoke init instruction.
     * @return true if this Instruction is a record invoke init instruction, false if not
     */
    default boolean isRecordInvokeInit(){return false;}
    
    /**
     * Gets the type of an Instruction.
     * @return the type of an Instruction
     */
    default String getType(){return "(V)";}
    
    /**
     * Tests if an Instruction is an invoke virtual instruction.
     * @return true if this Instruction is an invoke virtual instruction, false if not
     */
    default boolean isInvokeVirtual(){return false;}
    
    /**
     * Tests if an Instruction is a new instruction.
     * @return true if this Instruction is a new instruction, false if not
     */
    default boolean isNew(){return false;}
    
    /**
     * Tests if an Instruction is a try-catch block instruction.
     * @return true if this Instruction is a try-catch block instruction, false if not
     */
    default boolean isTryCatchBlock(){return false;}

    /**
     * Tests if an Instruction is a Method instruction which invoke the addSuppressed method from java.lang.Throwable.
     * @return true if this Instruction is a Method instruction which invoke the addSuppressed method from java.lang.Throwable, false if not
     */
    default boolean isAddSuppressedInvoke(){return false;}

    default boolean isInvokeDynamicInstruction(){return false;}

    default boolean isMethodInstruction(){return false;}

    default boolean isGetFieldInstruction(){return false;}

    default Optional<String> getName(){return Optional.empty();}

    default Optional<String> getOwner(){return Optional.empty();}

    default Optional<Integer> getVar(){return Optional.empty();}
}
