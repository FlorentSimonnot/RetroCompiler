package fr.project.instructions.features;

import java.util.Arrays;
import java.util.StringJoiner;
import fr.project.instructions.simple.Instruction;
import fr.project.instructions.simple.InstructionsCollector;
import fr.project.instructions.simple.MethodInstruction;
import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * A class that allows to write a concatenation instruction in bytecode into different versions.
 * It is stored as an Instruction of a Method. And this Method object is used into a MethodVisitor also used into a ClassVisitor.
 * The ClassVisitor will visit a .class file and write a code block in bytecode corresponding to a concatenation from the .class file on a new .class file according to the version required.
 * @author SIMONNOT Florent
 *
 */

public class ConcatenationInstruction implements Instruction {
    private static final int VERSION = Opcodes.V9;
    private final InstructionsCollector instructions;
    private final Type[] arguments;
    private final List<String> format;

    /**
     * Creates a new ConcatenationInstruction.
     * @param instructions - the instructions block attached to this concatenation instruction
     * @param format - the format of the line corresponding to the concatenation code block
     */
    public ConcatenationInstruction(InstructionsCollector instructions, List<String> format, Type[] arguments){
        this.instructions = instructions;
        this.format = format;
        this.arguments = arguments;
    }

    /**
     * Writes the bytecode corresponding to the concatenation instruction according to the version given.
     * @param version - the target version
     * @param mv - the MethodVisitor object attached to the .class file
     * @param lastInstruction - the last instruction writen
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        if(version < VERSION){
            writeInstructionOldVersion(version, mv, lastInstruction, className);
        }
        else {
            writeInstructionNewVersion(version, mv, lastInstruction, className);
        }
    }

    private void writeInstructionNewVersion(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        for(var i = 0; i < instructions.size(); i++) {
            var instruction = instructions.getInstruction(i);
            instruction.writeInstruction(version, mv, lastInstruction, className);
            lastInstruction = instruction;
        }
    }

    private void writeInstructionOldVersion(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        var type = Type.getType(StringBuilder.class);
        mv.visitTypeInsn(Opcodes.NEW, type.getInternalName());
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, type.getInternalName(), "<init>", "()V", false);

        var list = splitInstructions();
        var li = lastInstruction;
        var index = 0;

        /*System.err.println("size of list " + list.size());
        list.forEach(c -> {
            System.err.println(c);
            System.err.println("-----------");
        });
        System.err.println("*******************");*/

        for(String f : format){
            if(f.equals("arg")){
                var instructionsList = list.get(index);
                var i = 0;

                for(i = 0; i < instructionsList.size(); i++){
                    instructionsList.getInstruction(i).writeInstruction(version, mv, li, className);
                    li = instructionsList.getInstruction(i);
                }
                //li.writeInstruction(version, mv, li, className);

                //System.err.println("instruction : " + li + " type " + li.getType());
                System.err.println(li);

                if(li.isMethodInstruction()){
                    if(li.getName().isPresent() && li.getOwner().isPresent()){
                        if(li.getName().get().equals("<init>")){
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/Object;)" + "Ljava/lang/StringBuilder;", false);
                        }
                        else{
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", li.getType() + "Ljava/lang/StringBuilder;", false);
                        }
                    }
                }
                else{
                    //have to call toString method on the object
                    if(li.getType().contains("L")
                            && !li.getType().substring(1, li.getType().length()-2).equals("Ljava/lang/Object")
                            && !li.getType().substring(1, li.getType().length()-2).equals("Ljava/lang/String")){
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, li.getType().substring(2, li.getType().length()-2),  "toString", "()Ljava/lang/String;", false);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/String;)" + "Ljava/lang/StringBuilder;", false);
                    }
                    else if(li.getType().substring(1, li.getType().length()-2).equals("Ljava/lang/Object")){
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/Object;)" + "Ljava/lang/StringBuilder;", false);
                    }
                    //Box to java/lang/Byte for have toStringMethod
                    else if(li.getType().equals("(B)")){
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Byte",  "toString", "()Ljava/lang/String;", false);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    }
                    else {
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", li.getType() + "Ljava/lang/StringBuilder;", false);
                    }
                }
                index++;
            }
            else{
                System.err.println("format " + f);
                mv.visitLdcInsn(f);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                li = new MethodInstruction(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
        }
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "toString", "()Ljava/lang/String;", false);
    }

    private List<InstructionsCollector> splitInstructions(){
        var list = new ArrayList<InstructionsCollector>();
        var collector = new InstructionsCollector();
        var currentTypeIndex = 0;

        for(var i = 0; i < instructions.size()-1; i++){
            var instruction = instructions.getInstruction(i);

            var type = instruction.getType().substring(1, instruction.getType().length()-1);
            //System.err.println(instruction + " type " + type);

            if(currentTypeIndex < arguments.length) {
                if (instruction.isLoadInstruction() && !instructions.getInstruction(i + 1).isGetFieldInstruction()) {
                    if (!arguments[currentTypeIndex].toString().contains("L")) {
                        if (type.equals(arguments[currentTypeIndex].toString())) {
                            collector.add(instruction);
                            if (collector.size() > 0) {
                                list.add(collector);
                                collector = new InstructionsCollector();
                            }
                            currentTypeIndex++;
                        } else {
                            collector.add(instruction);
                        }
                    } else {
                        if (type.equals("Ljava/lang/Object;")){
                            collector.add(instruction);
                            if (collector.size() > 0) {
                                list.add(collector);
                                collector = new InstructionsCollector();
                            }
                            currentTypeIndex++;
                        } else {
                            collector.add(instruction);
                        }
                    }
                }
                else if (instruction.isMethodInstruction()) {
                    if (instruction.getName().isPresent()) {
                        if (!instruction.getName().get().equals("<init>")) {
                            if (type.equals(arguments[currentTypeIndex].toString())) {
                                collector.add(instruction);
                                if (collector.size() > 0) {
                                    list.add(collector);
                                    collector = new InstructionsCollector();
                                }
                                currentTypeIndex++;
                            } else {
                                collector.add(instruction);
                            }
                        } else {
                            if (instruction.getOwner().isPresent()) {
                                if (("L" + instruction.getOwner().get() + ";").equals(arguments[currentTypeIndex].toString())) {
                                    collector.add(instruction);
                                    if (collector.size() > 0) {
                                        list.add(collector);
                                        collector = new InstructionsCollector();
                                    }
                                    currentTypeIndex++;
                                }
                            } else {
                                collector.add(instruction);
                            }
                        }
                    }
                }
                else if (instruction.isGetFieldInstruction()) {
                    if (type.equals(arguments[currentTypeIndex].toString())) {
                        collector.add(instruction);
                        if (collector.size() > 0) {
                            list.add(collector);
                            collector = new InstructionsCollector();
                        }
                        currentTypeIndex++;
                    } else {
                        collector.add(instruction);
                    }
                }
            }
            else {
                collector.add(instruction);
            }

        }
        list.add(collector);
        list = list.stream().filter(l -> l.size() > 0).collect(Collectors.toCollection(ArrayList::new));
        return list;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner("\n---> ");
        instructions.forEach(i -> joiner.add(i.toString()));
        return "Concatenation ( "+ Arrays.toString(arguments) +" ) : \n---> " + joiner;
    }

    /**
     * Returns if this instruction is an aload instruction.
     * @return false
     */
    @Override
    public boolean isLoadInstruction() {
        return false;
    }
}
