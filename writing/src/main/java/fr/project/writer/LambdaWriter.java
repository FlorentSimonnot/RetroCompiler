package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.stream.Collectors;

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
            lambdaMethod.addInstruction(new VarInstruction(Utils.getOpcodeOfType(Utils.takeCapture(lambdaInstruction.getDescriptor())), 0));
        }


        lambdaMethod.addInstruction(new TypeInstruction(Opcodes.NEW, ownerFileClassName+"$MyLambda"+index));
        lambdaMethod.addInstruction(new NopInstruction(Opcodes.DUP));
        lambdaMethod.addInstruction(new MethodInstruction(Opcodes.INVOKESPECIAL, ownerFileClassName+"$MyLambda"+index, "<init>", "(" + Utils.takeCapture(lambdaInstruction.getDescriptor()) + ")V", false));
        lambdaMethod.addInstruction(new NopInstruction(Opcodes.ARETURN));
        lambdaClass.addMethod(lambdaMethod);
    }

    private static Type[] getArgumentsWithoutCaptures(MyClass myClass, LambdaInstruction lambdaInstruction){
        var args = lambdaInstruction.getArgumentsType();
        return Arrays.stream(args).skip(myClass.getFields().size()).toArray(Type[]::new);
    }

    /**
     *
     *
     * @param lambdaClass
     * @param method
     */
    static void createLambdaCalledMethod(MyClass lambdaClass, Method method, LambdaInstruction lambdaInstruction, String className){
        var interfaceReturnType = Type.getReturnType(method.getDescriptor());
        var lambdaReturnType = lambdaInstruction.getReturnTypeOfLambda();
        var i = 0;

        //Load captures fields
        lambdaClass.getFields().forEach(f -> {
            method.addInstruction(new VarInstruction(Utils.getOpcodeOfType(f.getDescriptor()), 0));
            method.addInstruction(new FieldInstruction(f.getName(), lambdaClass.getClassName(), Opcodes.GETFIELD, f.getDescriptor()));
        });

        var interfaceArgs = lambdaInstruction.getArgumentsTypeOfLambda();
        var args = getArgumentsWithoutCaptures(lambdaClass, lambdaInstruction);

        //Load parameters
        for(var j = 0; j < interfaceArgs.length; j++ ){
            method.addInstruction(new VarInstruction(UtilsWriter.getLoadInAccordingToTheType(interfaceArgs[j].toString()), i));
            if(!interfaceArgs[j].equals(args[j])){
                UtilsWriter.cast(method, interfaceArgs[j], args[j]);
            }
        }

        method.addInstruction(new MethodInstruction(Opcodes.INVOKEVIRTUAL, lambdaInstruction.getMethodCalledOwner(), lambdaInstruction.getMethodCalledName(), lambdaInstruction.getMethodCalledDescriptor(),false));
        //System.err.println("Class : " + lambdaClass.getClassName() + " return calledMethod : " + lambdaReturnType + " vs " + " return interface : " + interfaceReturnType);
        if(!interfaceReturnType.equals(lambdaReturnType)){
            UtilsWriter.cast(method, lambdaReturnType, interfaceReturnType);
        }

        method.addInstruction(new NopInstruction(Utils.getOpcodeOfReturn(interfaceReturnType.toString())));
        lambdaClass.addMethod(method);

    }
}
