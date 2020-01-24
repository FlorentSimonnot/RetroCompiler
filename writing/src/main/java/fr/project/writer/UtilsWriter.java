package fr.project.writer;

import fr.project.instructions.simple.Method;
import fr.project.instructions.simple.MethodInstruction;
import fr.project.instructions.simple.NopInstruction;
import fr.project.instructions.simple.TypeInstruction;
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

    private static boolean isPrimitiveType(Type t){
        return !t.toString().contains("L");
    }

    private static boolean isWrappedType(Type t){
        switch(t.toString().replace(";", "").replace("Ljava/lang/", "")){
            case "Long" :
            case "Double" :
            case "Float" :
            case "Character" :
            case "Boolean" :
            case "Short" :
            case "Byte" :
            case "Integer" : return true;
            default: return false;
        }
    }

    //From primitive type to wrapper type
    private static void box(Method method, Type src, Type dst){
        var owner = "ljava/lang/";
        switch (src.toString()){
            case "J" : owner = owner + "Long"; break;
            case "I" : owner = owner + "Integer"; break;
            case "Z" : owner = owner + "Boolean"; break;
            case "D" : owner = owner + "Double"; break;
            case "F" : owner = owner + "Float"; break;
            case "B" : owner = owner + "Byte"; break;
            case "C" : owner = owner + "Character"; break;
            case "S" : owner = owner + "Short"; break;
            default: throw new IllegalArgumentException(src.toString() + " is not a primitive type");
        }
        method.addInstruction(new MethodInstruction(Opcodes.INVOKESTATIC, owner, "valueOf", "("+src.toString()+")"+owner+";", false));
    }

    //From wrapper type to primitive type
    private static void unBox(Method method, Type src, Type dst){
        var methodName = "";
        var returnDescriptor = "";
        switch (src.toString().replace(";", "")){
            case "Ljava/lang/Integer" : methodName = "int"; returnDescriptor = "I"; break;
            case "Ljava/lang/Long" : methodName = "long"; returnDescriptor = "J"; break;
            case "Ljava/lang/Boolean" : methodName = "boolean"; returnDescriptor = "Z"; break;
            case "Ljava/lang/Double" : methodName = "double"; returnDescriptor = "D"; break;
            case "Ljava/lang/Float" : methodName = "float"; returnDescriptor = "F"; break;
            case "Ljava/lang/Byte" : methodName = "byte"; returnDescriptor = "B"; break;
            case "Ljava/lang/Character" : methodName = "char"; returnDescriptor = "C"; break;
            case "Ljava/lang/Short" : methodName = "short"; returnDescriptor = "S"; break;
            default: throw new IllegalArgumentException(src.toString() + " is not a wrapper type");
        }
        method.addInstruction(new MethodInstruction(Opcodes.INVOKESTATIC, src.toString().substring(1, src.toString().length()-1), methodName+"Value", "()"+returnDescriptor+";", false));
    }

    static void cast(Method method, Type a, Type b){
        System.err.println(a + " " + b);
        if(isPrimitiveType(a) && isWrappedType(b)){
            box(method, a, b);
        }
        else if(isWrappedType(a) && isPrimitiveType(b)){
            unBox(method, a, b);
        }
        else if(isPrimitiveType(a) && isPrimitiveType(b)){
            if(b.toString().equals("D")){
                method.addInstruction(new NopInstruction(Opcodes.I2D));
            }
        }
        //Verify if cast is possible
        else{
            method.addInstruction(new TypeInstruction(Opcodes.CHECKCAST, b.getInternalName()));
        }
    }

}
