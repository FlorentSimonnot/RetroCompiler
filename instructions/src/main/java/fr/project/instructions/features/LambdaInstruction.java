package fr.project.instructions.features;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;

/**
 * A class that represents a lambda instruction of a .class file.
 * @author SIMONNOT Florent
 *
 */
public class LambdaInstruction {
    public static final int VERSION = Opcodes.V1_8;
    private final String name;
    private final String ownerClass;
    private final String descriptor;
    private final Handle methodHandle;
    private final Object[] args;

    /**
     * Creates a new LambdaInstruction.
     * @param name - the lambda's name
     * @param ownerClass - the lambda's owner class
     * @param descriptor - the lambda's descriptor
     * @param methodHandle - the method that handles the lambda
     * @param args - the lambda's arguments
     */
    public LambdaInstruction(String name, String ownerClass, String descriptor, Handle methodHandle, Object[] args) {
        this.name = name;
        this.ownerClass = ownerClass;
        this.descriptor = descriptor;
        this.methodHandle = methodHandle;
        this.args = Arrays.copyOf(args, args.length);
    }

    @Override
    public String toString() {
        return "LAMBDA " + name + " " + ownerClass;
    }

    /**
     * Gets the lambda's version.
     * @return the lambda's version
     */
    int getVersion(){return VERSION;}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LambdaInstruction)) return false;
        var lambda = (LambdaInstruction) obj;
        return lambda.name.equals(name) &&
                getReturnType().equals(lambda.getReturnType()) &&
                haveSameArgumentsType(lambda) &&
                ownerClass.equals(lambda.ownerClass);
    }

    private boolean haveSameArgumentsType(LambdaInstruction lambdaInstruction){
        var arguments = getArgumentsType();
        var lambdaInstructionArguments = lambdaInstruction.getArgumentsType();
        if(lambdaInstructionArguments.length != arguments.length){
            return false;
        }
        for(var i = 0; i < arguments.length; i++){
            if(!arguments[i].equals(lambdaInstructionArguments[i])) return false;
        }
        return true;
    }

    /**
     * Gets the lambda's name.
     * @return the lambda's name
     */
    public String getName(){return name;}

    /**
     * Gets the method that handles the lambda.
     * @return the method that handles the lambda
     */
    public Handle getMethodHandle(){return methodHandle;}

    /**
     * Gets the lambda's arguments.
     * @return the lambda's arguments
     */
    public Object[] getBootstrapMethodArguments(){return args;}

    /**
     * Gets the lambda's owner class.
     * @return the lambda's owner class
     */
    public String getOwnerClass(){return ownerClass;}

    /**
     * Gets the lambda's return type.
     * @return the lambda's return type
     */
    public Type getReturnType(){
        var method = (Handle) args[1];
        return Type.getReturnType(method.getDesc());
    }

    /**
     * Gets the lambda's arguments type.
     * @return the lambda's arguments type
     */
    public Type[] getArgumentsType(){
        var method = (Handle) args[1];
        return Type.getArgumentTypes(method.getDesc());
    }

    /**
     * Gets the method that calls the lambda.
     * @return the method that class the lambda
     */
    public String getMethodCalledName(){
        var methodCalled = (Handle) args[1];
        return methodCalled.getName();
    }

    /**
     * Gets the owner class of the method that calls the lambda.
     * @return the owner class of the method that class the lambda
     */
    public String getMethodCalledOwnerName(){
        var methodCalled = (Handle) args[1];
        return methodCalled.getOwner();
    }

    /**
     * Gets the descriptor of the method that calls the lambda.
     * @return the descriptor of the method that calls the lambda
     */
    public String getMethodCalledDescriptor(){
        return Type.getMethodDescriptor(getReturnType(), getArgumentsType());
    }

    /**
     * Gets the arguments type of the lambda.
     * @return the arguemnts type of the lambda
     */
    Type[] getCapture(){
        return Type.getArgumentTypes(descriptor);
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ methodHandle.hashCode() ^ ownerClass.hashCode() ^ Arrays.hashCode(args) ^ descriptor.hashCode();
    }

    /**
     * Gets the lambda's descriptor.
     * @return the lambda's descriptor
     */
    public String getDescriptor() {
        return descriptor;
    }
}
