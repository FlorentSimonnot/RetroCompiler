package fr.project.writer;

import fr.project.optionsCommand.Option;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class UtilsWriter {

    static int getReturnInAccordingToTheType(String descriptor){
        var type = Type.getType(descriptor);
        return type.getOpcode(Opcodes.IRETURN);
    }

    static int getLoadInAccordingToTheType(String descriptor){
        var type = Type.getType(descriptor);
        return type.getOpcode(Opcodes.ILOAD);
    }

    static int getDupOrDup2(String descriptor){
        var type = Type.getType(descriptor);
        if(type == Type.LONG_TYPE || type == Type.DOUBLE_TYPE){
            return Opcodes.DUP2_X1;
        }
        return Opcodes.DUP_X1;
    }

}
