package fr.project.instructions.simple;

import fr.project.instructions.features.ConcatenationInstruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 
 * A class that allows to store a list of Instruction.
 * @author CHU Jonathan
 *
 */
public class InstructionsCollector {
    private final List<Instruction> instructions;

    /**
     * Creates a new InstructionsCollector.
     */
    public InstructionsCollector(){instructions = new LinkedList<>();}

    /**
     * Gets the size of the list of Instruction.
     * @return the size of the field instructions
     */
    public int size(){return instructions.size();}

    /**
     * Gets an Instruction from the list of Instruction.
     * @param index - the index of the Instruction asked
     * @return the Instruction asked
     */
    public Instruction getInstruction(int index){
        if(index > -1)
            return instructions.get(index);
        throw new IllegalArgumentException("Index out of Bounds in instructions collector");
    }

    private Instruction getLastInstruction() {
        if(instructions.size() == 0)
            throw new IllegalArgumentException("List of instructions is empty");
        return instructions.get(instructions.size()-1);
    }

    /**
     * Adds an Instruction into the list of Instruction.
     * @param instruction - an Instruction you want to add to the field instructions
     */
    public void add(Instruction instruction) {
        instructions.add(instructions.size(), instruction);
    }

    /**
     * Adds a collection of Instruction to the list of Instruction.
     * @param collector - a Collection of Instruction
     */
    void addAll(Collection<? extends Instruction> collector){
        instructions.addAll(collector);
    }

    /**
     * Clears the list of Instruction.
     */
    void clear(){instructions.clear();}

    /**
     * Applies a function to the list of Instruction.
     * @param consumer - the function you want to apply to your InstructionCollector
     */
    public void forEach(Consumer<? super Instruction> consumer) {
        instructions.forEach(consumer);
    }

    /**
     * Writes all the Instruction of the InstructionsCollector according to a version into a .class file.
     * @param version - the target version
     * @param methodVisitor - the MethodVisitor linked to a .class file
     */
    void writeAllInstruction(int version, MethodVisitor methodVisitor, String className){
        Instruction lastInstruction = new NopInstruction(0);
        for(Instruction i : instructions){
            i.writeInstruction(version, methodVisitor, lastInstruction, className);
            lastInstruction = i;
        }
    }

    /**
     * Displays all Instruction of the InstructionsCollector
     * @param indent - an int used for the text format
     * @return the text format for all the Instruction.
     */
    String printAllInstruction(int indent){
        var sb = new StringBuilder();
        for(Instruction i : instructions){
            sb.append("-".repeat(indent)).append("> ").append(i).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(" \n");
        instructions.forEach(i -> joiner.add(i.toString()));
        return joiner.toString();
    }

    /**
     * Creates a concatenation of all Instruction.
     * @param nArgs - a number of arguments
     * @param format - a text format
     * @return a new list of Instruction
     */
    List<Instruction> createConcatenationInstruction(int nArgs, List<String> format, String descriptor) {
        var newCollector = new InstructionsCollector();
        var concatCollector = new InstructionsCollector();
        var count = 0;
        var types = Type.getArgumentTypes(descriptor);
        var currentTypeIndex = types.length-1;
        var previousInstruction = instructions.get(instructions.size()-1);

        var i = size()-1;
        System.err.println(nArgs);
        for(; i >= 0; i--){
            var e = instructions.get(i);
            var instructionsType = e.getType().replace("(", "").replace(")", "");
            if(count < nArgs){
                System.err.println(count + " " + e);
                if(e.isLoadInstruction() && !previousInstruction.isGetFieldInstruction()){
                    if(!types[currentTypeIndex].toString().contains("L")){
                        if(instructionsType.equals(types[currentTypeIndex].toString())){
                            count++;
                            currentTypeIndex--;
                        }
                    }
                    else{
                        if(instructionsType.equals("Ljava/lang/Object;")){
                            count++;
                            currentTypeIndex--;
                        }
                    }
                }
                else if(e.isMethodInstruction()) {
                    if(e.getName().isPresent()) {
                        if (!e.getName().get().equals("<init>")) {
                            if (instructionsType.equals(types[currentTypeIndex].toString())) {
                                count++;
                                currentTypeIndex--;
                            }
                        }
                        else{
                            if(e.getOwner().isPresent()) {
                                if (("L"+e.getOwner().get()+";").equals(types[currentTypeIndex].toString())) {
                                    count++;
                                    currentTypeIndex--;
                                }
                            }
                        }
                    }
                }
                else if(e.isGetFieldInstruction()){
                    if(instructionsType.equals(types[currentTypeIndex].toString())){
                        count++;
                        currentTypeIndex--;
                    }
                }
                concatCollector.add(e);
            }
            else{
                if(concatCollector.getInstruction(concatCollector.size()-1).isGetFieldInstruction())
                    concatCollector.add(e);
                else
                    newCollector.add(e);
            }
            previousInstruction = e;
        }

        var res = new InstructionsCollector();
        Collections.reverse(concatCollector.instructions);
        Collections.reverse(newCollector.instructions);
        res.addAll(newCollector.instructions);
        format = format.stream().filter(f -> f.length() > 0).collect(Collectors.toList());
        res.add(new ConcatenationInstruction(concatCollector, format, types));
        return res.instructions;
    }

    public boolean containsInvokeDynamicInstructionAccordingToAName(MyClass myClass, String name){
        Objects.requireNonNull(myClass);
        Objects.requireNonNull(name);
        for (Instruction instruction : instructions) {
            if(instruction.isInvokeDynamicInstruction()){
                if(instruction.getName().isPresent()){
                    if(name.equals(instruction.getName().get())){
                        if(instruction.getOwner().isPresent()){
                            var className = instruction.getOwner().get();
                            return className.substring(1, className.length()-1).equals(myClass.getClassName());
                        }
                    }
                }
            }
        }
        return false;
    }

}
