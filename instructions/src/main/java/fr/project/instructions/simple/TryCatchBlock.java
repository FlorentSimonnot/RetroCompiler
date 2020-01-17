package fr.project.instructions.simple;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

/**
 * 
 * A class that allows to detect and write a try catch block instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class TryCatchBlock implements Instruction {
    private final TryCatchBlockGroup tryCatchBlockGroup;

    private TryCatchBlock(TryCatchBlockGroup group) {
        this.tryCatchBlockGroup = group;
    }

    /**
     * Creates a new TryCatchBlock.
     * @param instruction - the instructions of the try catch block
     * @return a new TryCatchBlock object
     */
    public static TryCatchBlock createTryCatchBlock(TryCatchBlockInstruction instruction){
        return new TryCatchBlock(new TryCatchBlockGroup(instruction));
    }

    /**
     * Adds an Instruction at the end of the try catch block.
     */
    public void addInstruction(Instruction instruction){
        for(var i = this.tryCatchBlockGroup; i != null; i = i.next){
            if(!i.isClosed){
                i.addInstruction(instruction);
                return;
            }
        }
    }

    /**
     * Adds a TryCatchBlockInstruction at the end of the TryCatchBlockGroup.
     * @param instruction - a TryCatchBlockInstruction to add
     */
    public void addTryCatchBlockGroup(TryCatchBlockInstruction instruction){
        var i = this.tryCatchBlockGroup;
        while(i.next != null){i = i.next;}
        i.next = new TryCatchBlockGroup(instruction);
    }

    /**
     * Gets all the closed try catch blocks.
     * @return all closed try catch blocks
     */
    public ArrayList<Label> allOfClosedTryCatchBlock(){
        var res = new ArrayList<Label>();
        for(var i = this.tryCatchBlockGroup; i != null; i = i.next){
            if(i.isClosed){
                res.add(i.instruction.getStart());
            }
        }
        return res;
    }

    /**
     * Closes the last try catch block instruction of the group.
     * @param label - the end of the exception handler's scope
     * @return true if the last try catch block instruction is successfully closed, false if not
     */
    public boolean closeLastTryCatchBlock(Label label){
        if(isClosed()){
            throw new IllegalStateException("Block is already close");
        }
        for(var i = this.tryCatchBlockGroup; i != null; i = i.next){
            if(!i.isClosed && i.isEndLabel(label)){
                i.closeTryCatchBlock();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("Try Catch Block : Size = " + sizeOfTryCatchBlock() + " \n");
        var indent = 1;
        for(var i = tryCatchBlockGroup; i != null; i = i.next){
            sb.append(i.printGroup(indent));
            indent++;
        }
        return sb.toString();
    }

    /**
     * Gets the number of try catch blocks in the TryCatchBlockGroup.
     * @return the number of try catch blocks
     */
    int sizeOfTryCatchBlock(){
        var count = 0;
        for(var i = tryCatchBlockGroup; i != null; i = i.next){
            count++;
        }
        return count;
    }

    /**
     * Tests if the TryCatchBlockGroup is closed.
     * @return true if it is closed, false if not
     */
    public boolean isClosed(){
        return tryCatchBlockGroup.isClosed;
    }

    /**
     * Writes all the try catch block instructions into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className)  {
        tryCatchBlockGroup.writeAllInstruction(version, mv, lastInstruction, className);
    }

    private static class TryCatchBlockGroup {
        private boolean isClosed = false;
        TryCatchBlockInstruction instruction;
        private final InstructionsCollector instructions = new InstructionsCollector();
        private TryCatchBlockGroup next = null;

        private TryCatchBlockGroup(TryCatchBlockInstruction instruction) {
            this.instruction = instruction;
        }

        private void addInstruction(Instruction instruction){
            instructions.add(instruction);
        }

        private boolean isEndLabel(Label label){
            return instruction.getEnd().equals(label);
        }

        private void closeTryCatchBlock(){
            isClosed = true;
        }

        private String printGroup(int indent) {
            return "TryCatchGroup : BEGIN AT " + instruction.getStart() + " CLOSE AT  " + instruction.getEnd() + " \n" + instructions.printAllInstruction(indent);
        }

        private void writeAllInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className){
            this.instructions.writeAllInstruction(version, mv, className);
        }

    }
}
