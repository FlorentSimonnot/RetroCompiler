package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class LambdaWriter {

    /**
     * static void createInterface(MyClass lambdaClass)
     * Add an interface in the lambda class
     * @param lambdaClass - the class which represents the lambda
     */
    static void createInterface(MyClass lambdaClass){
        lambdaClass.addInterface("interface"+lambdaClass.getClassName());
    }

    /**
     * static void createFields(MyClass lambdaClass, LambdaInstruction lambdaInstruction)
     * Create fields for the captures
     * @param lambdaClass - the class which represents the lambda
     * @param lambdaInstruction - an object which represents the lambda
     */
    static void createFields(MyClass lambdaClass, LambdaInstruction lambdaInstruction){
        if(Utils.takeCapture(lambdaInstruction.getDescriptor()).length() > 0) {
            var field = new Field(Opcodes.ACC_PRIVATE, "field$0", Utils.takeCapture(lambdaInstruction.getDescriptor()), null, null);
            lambdaClass.addField(field);
        }
    }

    /**
     * static void createConstructor(MyClass lambdaClass, LambdaInstruction lambdaInstruction)
     * Create the constructor of the lambda class
     * @param lambdaClass - the class which represents the lambda
     * @param lambdaInstruction - an object which represents the lambda
     */
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

    /**
     * static void createLambdaFactory(MyClass lambdaClass, LambdaInstruction lambdaInstruction, String ownerFileClassName, int index)
     * Create the static method to create the Lambda class and add it as a method in the lambda's class.
     * @param lambdaClass - class which represents a lambda
     * @param lambdaInstruction - the lambda instruction
     * @param ownerFileClassName - the class which calls the lambda
     * @param index - the current index corresponds to the number of Lambdas.
     */
    static void createLambdaFactory(MyClass lambdaClass, LambdaInstruction lambdaInstruction, String ownerFileClassName, int index){
        var lambdaMethod = new Method(Opcodes.ACC_STATIC, "myLambdaFactory$"+index, "("+Utils.takeCapture(lambdaInstruction.getDescriptor())+")Linterface"+ownerFileClassName+"$MyLambda"+index+";", null, false, null);
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

    /**
     *
     *
     * @param lambdaClass
     * @param method
     */
    static void createLambdaCalledMethod(MyClass lambdaClass, Method method){
        //load this
        method.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
        method.printInstructions();
        lambdaClass.addMethod(method);

    }
}
