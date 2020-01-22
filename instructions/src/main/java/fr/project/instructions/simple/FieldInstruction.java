package fr.project.instructions.simple;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Objects;

/**
 * 
 * A class that allows to detect and write a field instruction of a .class file.
 * It is stored as an Instruction of a Method.
 * @author CHU Jonathan
 *
 */
public class FieldInstruction implements Instruction {
    private final String name;
    private final String owner;
    private final int opcode;
    private final String descriptor;
    public static final int VERSION = Opcodes.V11;

    /**
     * Creates a new FieldInstruction.
     * @param name - the name of the field
     * @param owner - the name of the Method that contains the field
     * @param opcode - the opcode of the instruction to be visited
     * @param descriptor - the descriptor of the field
     */
    public FieldInstruction(String name, String owner, int opcode, String descriptor) {
        if(opcode < 0) throw new IllegalArgumentException("Opcode must be positive");
        this.name = Objects.requireNonNull(name);
        this.owner = Objects.requireNonNull(owner);
        this.opcode = opcode;
        this.descriptor = Objects.requireNonNull(descriptor);
    }

    /**
     * Gets the type of the field instruction.
     * @return the type of the field instruction
     */
    public String getType()    {
        switch(opcode) {
            case Opcodes.ILOAD:
                return "(I)";
            case Opcodes.LLOAD:
                return "(J)";
            case Opcodes.DLOAD:
                return "(D)";
            case Opcodes.ALOAD:
                return "(Ljava/lang/Object;)";
            case Opcodes.PUTFIELD :
            case Opcodes.GETFIELD :
                return "("+descriptor+")";
            default:
                return "()";
        }
    }

    /**
     * Writes the field instruction into a .class file.
     */
    @Override
    public void writeInstruction(int version, MethodVisitor mv, Instruction lastInstruction, String className) {
        if(version < VERSION){
            if(owner.contains("$") && !className.contains("$")){
                if (opcode == Opcodes.GETFIELD) {
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, owner, "accessGetter"+name.toUpperCase(), "(L"+owner+";)"+descriptor, false);
                }
                if(opcode == Opcodes.PUTFIELD){
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, owner, "accessSetter"+name.toUpperCase(), "(L"+owner+";"+descriptor+")"+descriptor, false);
                    if(Type.getType(descriptor) == Type.LONG_TYPE || Type.getType(descriptor) == Type.DOUBLE_TYPE)
                        mv.visitInsn(Opcodes.POP2);
                    else
                        mv.visitInsn(Opcodes.POP);
                }
                return;
            }
        }
        if(opcode == Opcodes.GETFIELD && !lastInstruction.isAloadInstruction()) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
        }
        mv.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public String toString() {
        return "FIELD INSN name : " + name + " owner : " + owner + " opcode : " + opcode + " descriptor : " + descriptor;
    }
}
