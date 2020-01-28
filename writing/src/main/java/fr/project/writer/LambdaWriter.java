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
        System.err.println("size : " + lambdaClass.getFields().size());
        var i = 1;
        for(Field f : lambdaClass.getFields()){
            lambdaConstructor.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
            i = UtilsWriter.load(lambdaConstructor, UtilsWriter.getLoadInAccordingToTheType(f.getDescriptor()), i);
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

        lambdaMethod.addInstruction(new TypeInstruction(Opcodes.NEW, ownerFileClassName+"$MyLambda"+index));
        lambdaMethod.addInstruction(new NopInstruction(Opcodes.DUP));

        var i = 0;
        for(Field f : lambdaClass.getFields()){
            //lambdaMethod.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
            i = UtilsWriter.load(lambdaMethod, UtilsWriter.getLoadInAccordingToTheType(f.getDescriptor()), i);
            //lambdaConstructor.addInstruction(new VarInstruction(Utils.getOpcodeOfType(Utils.takeCapture(lambdaInstruction.getDescriptor())), 0));
            //lambdaMethod.addInstruction(new FieldInstruction(f.getName(), lambdaClass.getClassName(), Opcodes.PUTFIELD, f.getDescriptor()));
        }

        lambdaMethod.addInstruction(new MethodInstruction(Opcodes.INVOKESPECIAL, ownerFileClassName+"$MyLambda"+index, "<init>", "(" + Utils.takeCapture(lambdaInstruction.getDescriptor()) + ")V", false));
        lambdaMethod.addInstruction(new NopInstruction(Opcodes.ARETURN));
        lambdaClass.addMethod(lambdaMethod);
    }

    private static Type[] getArgumentsWithoutCaptures(MyClass myClass, LambdaInstruction lambdaInstruction){
        var args = lambdaInstruction.getArgumentsType();
        for(Type t : args){
            System.err.println(t);
        }
        return Arrays.stream(args).skip(myClass.getFields().size()).toArray(Type[]::new);
    }

    /**
     *
     *
     * @param lambdaClass
     * @param method
     */
    static void createLambdaCalledMethod(MyClass lambdaClass, Method method, LambdaInstruction lambdaInstruction, String className){
        var interfaceReturnType = lambdaInstruction.getInterfaceReturnType();
        var lambdaReturnType = lambdaInstruction.getReturnTypeOfLambda();
        var i = 1;

        //Load captures fields
        lambdaClass.getFields().forEach(f -> {
            method.addInstruction(new VarInstruction(Opcodes.ALOAD, 0));
            method.addInstruction(new FieldInstruction(f.getName(), lambdaClass.getClassName(), Opcodes.GETFIELD, f.getDescriptor()));
        });

        var interfaceArgs = lambdaInstruction.getInterfaceArgumentsType();
        var args = lambdaInstruction.getArgumentsTypeOfLambda();
        var methodCalledArgs = getArgumentsWithoutCaptures(lambdaClass, lambdaInstruction);


        //Load parameters
        for(var j = 0; j < interfaceArgs.length; j++ ){
            i = UtilsWriter.load(method, UtilsWriter.getLoadInAccordingToTheType(interfaceArgs[j].toString()), i);
            //Cast from interface to method handle arguments
            if(!interfaceArgs[j].equals(args[j])){
                UtilsWriter.cast(method, interfaceArgs[j], args[j]);
            }
            //Cast from method handle arguments to method called by the lambda
            if(!args[j].equals(methodCalledArgs[j])){
                UtilsWriter.cast(method, args[j], methodCalledArgs[j]);
            }
        }

        if(lambdaInstruction.getBridgeMethod().isPresent()){
            var bridgeMethod = lambdaInstruction.getBridgeMethod().get();
            method.addInstruction(new MethodInstruction(Opcodes.INVOKESTATIC, className, bridgeMethod.getName(), bridgeMethod.getDescriptor(), false));
        }
        else {
            method.addInstruction(new MethodInstruction(Opcodes.INVOKESTATIC, lambdaInstruction.getMethodCalledOwner(), lambdaInstruction.getMethodCalledName(), lambdaInstruction.getMethodCalledDescriptor(),false));
        }
        if(!interfaceReturnType.equals(lambdaReturnType)){
            UtilsWriter.cast(method, lambdaReturnType, interfaceReturnType);
        }

        method.addInstruction(new NopInstruction(Utils.getOpcodeOfReturn(interfaceReturnType.toString())));
        lambdaClass.addMethod(method);

    }

    /**
     * Create a bridge method in the ownerClass if the method use by the lambda is private.
     * @param ownerClass - the class which contain the lambda call
     * @param lambdaInstruction - a lambda
     */
    static void createBridgeMethod(MyClass ownerClass, LambdaInstruction lambdaInstruction, int index){
        if(ownerClass.getClassName().equals(lambdaInstruction.getMethodCalledOwner())){
            var method = ownerClass.getMethodByName(lambdaInstruction.getMethodCalledName(), lambdaInstruction.getMethodCalledDescriptor());
            if(method.isPrivateMethod()){
                System.err.println(method + " is a private method");
                var bridgeMethod = new Method(Opcodes.ACC_STATIC+Opcodes.ACC_PUBLIC, "bridgeMyLambda$"+index, method.getDescriptor(), method.getSignature(), false, null);
                var i = 0;
                //Load parameters
                for(Type t : Type.getArgumentTypes(bridgeMethod.getDescriptor())){
                    i = UtilsWriter.load(bridgeMethod, UtilsWriter.getLoadInAccordingToTheType(t.getDescriptor()), i);
                }
                bridgeMethod.addInstruction(new MethodInstruction(Opcodes.INVOKESTATIC, ownerClass.getClassName(), lambdaInstruction.getMethodCalledName(), lambdaInstruction.getMethodCalledDescriptor(), false));
                bridgeMethod.addInstruction(new NopInstruction(UtilsWriter.getReturnInAccordingToTheType(Type.getReturnType(bridgeMethod.getDescriptor()).toString())));
                ownerClass.addMethod(bridgeMethod);
                lambdaInstruction.setBridgeMethod(bridgeMethod);
            }
        }
    }
}
