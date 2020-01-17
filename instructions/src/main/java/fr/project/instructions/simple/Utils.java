package fr.project.instructions.simple;

import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides tools to write instruction in java bytecode.
 * @author CHU Jonathan
 *
 */
public class Utils {

	/**
	 * Converts a function's full name to a certain format that contains its owner class.
	 * @param name - the function's full name
	 * @return the text format for the function's name.
	 */
    public static String takeOwnerFunction(String name){
        var split = name.split("L");
        if(split.length != 2){
            throw new IllegalStateException();
        }
        return split[1].replace(";", "");
    }

    /**
     * Gets the arguments to be captured in a method handle.
     * @param name - the method's name
     * @return the text format that contains arguments to be captured.
     */
    public static String takeCapture(String name){
        var split = name.split("L");
        if(split.length != 2){
            throw new IllegalStateException();
        }
        return split[0].replace("(", "").replace(")", "");
    }

    /**
     * Converts an java opcode into a certain text format.
     * @param opcode - a java opcode
     * @return the text format for the java opcode.
     */
    public static String getOwnerOfVarInstruction(int opcode){
        switch(opcode){
            case Opcodes.ILOAD : return "(I)";
            case Opcodes.LLOAD : return "(J)";
            case Opcodes.DLOAD : return "(D)";
            case Opcodes.ALOAD : return "(java/lang/Object;)";
            default: throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the java opcode for a type.
     * @param type - a type code
     * @return the translation of a type code into java opcode
     */
    public static int getOpcodeOfType(String type){
        switch(type){
            case "J" : return Opcodes.LLOAD;
            case "D" : return Opcodes.DLOAD;
            case "I" : return Opcodes.ILOAD;
            case "F" : return  Opcodes.FLOAD;
            default : return Opcodes.ALOAD;
        }
    }

    /**
     * Gets the java opcode for a return type.
     * @param type - a return type code
     * @return the translation of a return type code into java opcode
     */
    public static int getOpcodeOfReturn(String type){
        switch(type){
            case "J" : return Opcodes.LRETURN;
            case "D" : return Opcodes.DRETURN;
            case "I" : return Opcodes.IRETURN;
            case "F" : return Opcodes.FRETURN;
            default : return Opcodes.ARETURN;
        }
    }

    /**
     * Creates a list of constants for concatenation into a text format.
     * @param format - the old format for the constants for concatenation
     * @return the new format for the constants for concatenation.
     */
    public static List<String> createListOfConstantForConcatenation(String format){
        var list = new ArrayList<String>();
        var word = new StringBuilder();

        for(var i = 0; i < format.toCharArray().length; i++){
            char c = format.toCharArray()[i];
            if(c == '\u0001'){
                if(word.length() > 0)
                    list.add(word.toString());
                list.add("arg");
                word = new StringBuilder("");
            }
            else{
                word.append(c);
            }
        }
        list.add(word.toString());

        return list;
    }

    /**
     * Gets the number of occurence of a certain word in a text.
     * @param format - the text to parse
     * @param word - the word to count
     * @return the number of occurence of the word.
     */
    public static int numberOfOccurrence(List<String> format, String word){
        var count = 0;
        for(String s : format){
            if(s.equals(word)){count++;}
        }
        return count;
    }

}
