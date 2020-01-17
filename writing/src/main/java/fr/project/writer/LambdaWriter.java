package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class LambdaWriter {

    static void createFields(MyClass lambdaClass, LambdaInstruction lambdaInstruction){
        if(Utils.takeCapture(lambdaInstruction.getDescriptor()).length() > 0) {
            var field = new Field(Opcodes.ACC_PRIVATE, "field$0", Utils.takeCapture(lambdaInstruction.getDescriptor()), null, null);
            lambdaClass.addField(field);
        }
    }

    static void createConstructor(MyClass lambdaClass, LambdaInstruction lambdaInstruction){
        var lambdaConstructor = new Method(Opcodes.ACC_PRIVATE, "<init>", "(" + Utils.takeCapture(lambdaInstruction.getDescriptor()) + ")V", null, false, null);
        lambdaConstructor.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
        lambdaConstructor.addInstruction(new MethodInstruction(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
        if(Utils.takeCapture(lambdaInstruction.getDescriptor()).length() > 0) {
            lambdaConstructor.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
            lambdaConstructor.addInstruction(new VarInstruction(Utils.getOpcodeOfType(Utils.takeCapture(lambdaInstruction.getDescriptor())), 0));
            lambdaConstructor.addInstruction(new FieldInstruction("field$0", lambdaClass.getClassName(), Opcodes.PUTFIELD, Utils.takeCapture(lambdaInstruction.getDescriptor())));
        }
        lambdaConstructor.addInstruction(new NopInstruction(Opcodes.RETURN));
        lambdaClass.addMethod(lambdaConstructor);
    }

    static void createLambdaFactory(MyClass lambdaClass, LambdaInstruction lambdaInstruction, String ownerFileClassName, int index){
        var lambdaMethod = new Method(Opcodes.ACC_STATIC, "myLambdaFactory$"+index, "("+Utils.takeCapture(lambdaInstruction.getDescriptor())+")L"+ownerFileClassName+"$MyLambda"+index+";", null, false, null);
        if(Utils.takeCapture(lambdaInstruction.getDescriptor()).length() > 0) {
            lambdaMethod.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
            lambdaMethod.addInstruction(new VarInstruction(Utils.getOpcodeOfType(Utils.takeCapture(lambdaInstruction.getDescriptor())), 0));
        }
        lambdaMethod.addInstruction(new TypeInstruction(Opcodes.NEW, ownerFileClassName+"$MyLambda"+index));
        lambdaMethod.addInstruction(new NopInstruction(Opcodes.DUP));
        lambdaMethod.addInstruction(new MethodInstruction(Opcodes.INVOKESPECIAL, ownerFileClassName+"$MyLambda"+index, "<init>", "(" + Utils.takeCapture(lambdaInstruction.getDescriptor()) + ")V", false));
        lambdaMethod.addInstruction(new NopInstruction(Opcodes.ARETURN));
        lambdaClass.addMethod(lambdaMethod);
    }

    static void createLambdaCalledMethod(MyClass lambdaClass, LambdaInstruction lambdaInstruction, String ownerFileClassName, int index){
        var lambdaMethod = new Method(Opcodes.ACC_PUBLIC, "myLambdaFunction$"+index, lambdaInstruction.getMethodCalledDescriptor(), null, false, null);
        //lambdaMethod.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
        for(Type t : Type.getArgumentTypes(lambdaInstruction.getMethodCalledDescriptor())){
            lambdaMethod.addInstruction(new VarInstruction(Utils.getOpcodeOfType(t.toString()), 1));
        }
        lambdaMethod.addInstruction(new MethodInstruction(Opcodes.INVOKESTATIC, lambdaInstruction.getMethodCalledOwnerName(), lambdaInstruction.getMethodCalledName(), Type.getMethodDescriptor(lambdaInstruction.getReturnType(), lambdaInstruction.getArgumentsType()), false));
        lambdaMethod.addInstruction(new NopInstruction(Utils.getOpcodeOfReturn(lambdaInstruction.getReturnType().toString())));
        lambdaClass.addMethod(lambdaMethod);

    }
}
