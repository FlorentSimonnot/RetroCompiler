package fr.project.instructions.features;

import java.util.StringJoiner;
import fr.project.instructions.simple.Instruction;
import fr.project.instructions.simple.InstructionsCollector;
import fr.project.instructions.simple.MethodInstruction;
import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.ArrayList;
import java.util.List;

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
    private final List<String> format;

    /**
     * Creates a new ConcatenationInstruction.
     * @param instructions - the instructions block attached to this concatenation instruction
     * @param format - the format of the line corresponding to the concatenation code block
     */
    public ConcatenationInstruction(InstructionsCollector instructions, List<String> format){
        this.instructions = instructions;
        this.format = format;
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
        for(String f : format){
            if(f.equals("arg")){
                var instructionsList = list.get(index);
                for(var i = 0; i < instructionsList.size(); i++){
                    instructionsList.getInstruction(i).writeInstruction(version, mv, li, className);
                    li = instructionsList.getInstruction(i);
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.getInternalName(), "append", li.getType() + "Ljava/lang/StringBuilder;", false);
                index++;
            }
            else{
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
        for(var i = 0; i < instructions.size()-1; i++){
            var instruction = instructions.getInstruction(i);
            if(instruction.isAloadInstruction() || instruction.isNew()){
                if(collector.size() > 0){
                    list.add(collector);
                    collector = new InstructionsCollector();
                    collector.add(instruction);
                }
                else{
                    collector.add(instruction);
                }
            }
            else {
                collector.add(instruction);
            }
        }
        list.add(collector);
        return list;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner("\n---> ");
        instructions.forEach(i -> joiner.add(i.toString()));
        return "Concatenation : \n---> " + joiner;
    }

    /**
     * Returns if this instruction is an aload instruction.
     * @return false
     */
    @Override
    public boolean isAloadInstruction() {
        return false;
    }
}
