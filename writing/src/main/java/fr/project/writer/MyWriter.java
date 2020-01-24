package fr.project.writer;

import fr.project.instructions.features.LambdaInstruction;
import fr.project.instructions.simple.*;
import fr.project.optionsCommand.Options;
import fr.project.warningObservers.WarningNestMemberObserver;
import fr.project.warningObservers.WarningObserver;
import fr.project.warningObservers.WarningsManager;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 
 * A class that allows to write a new .class file according to a .class file you visit.
 * @author CHU Jonathan
 *
 */
public class MyWriter implements Writer{
    private MyClass myClass;
    private final ClassWriter cw;
    private final int version;
    private MethodVisitor mw;
    private final List<WarningObserver> warningObservers;
    private final Options options;

    /**
     * Creates a new MyWriter.
     * @param myClass - the .class file you want to link your MyWriter with
     * @param version - the target version of your new .class file
     * @param warningObservers - a list of WarningObserver
     * @param options - an Options object
     */
    public MyWriter(MyClass myClass, int version, List<WarningObserver> warningObservers, Options options){
        this.myClass = myClass;
        this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        this.version = version;
        this.warningObservers = warningObservers;
        this.options = options;
    }

    /**
     * Creates the new .class file according to its privacy and its name.
     */
    @Override
    public void createClass(){
        cw.visit(version, myClass.getPrivacy(), myClass.getClassName(), null, "java/lang/Object", myClass.getInterfaces());
        myClass.getInnerClasses().forEach(this::writeInnerClass);
        writeSourceFile(myClass.getSourceName());

        //We can write NestHost and nestMates because version is accepted.
        if(version >= FieldInstruction.VERSION){
            myClass.getNestMembers().forEach(this::writeNestMember);
            myClass.getNestHosts().forEach(this::writeNestHost);
        }
    }

    /**
     * Writes all the lambdas of a .class file into a new .class file according to a target version.
     */
    @Override
    public void writeLambdaInnerClasses(){
        if(version < LambdaInstruction.VERSION && options.forceIsDemanding())
            myClass.getLambdaCollector().forEach(this::writeLambdaInnerClass);
        else{
            myClass.getLambdaCollector().forEach( (l, k) -> {
                warningObservers.forEach(o -> o.onWarningDetected("We have detected a lambda", "lambda"));
            });
        }
    }

    private void writeLambdaInnerClass(LambdaInstruction lambdaInstruction, int index){
        cw.visitInnerClass(myClass.getClassName()+"$MyLambda"+index, myClass.getClassName(), myClass.getClassName()+"$MyLambda"+index, Opcodes.ACC_PRIVATE+Opcodes.ACC_STATIC);
    }

    @Override
    public void writeLambdaFiles(){
        if(version < LambdaInstruction.VERSION && options.forceIsDemanding())
            myClass.getLambdaCollector().forEach(this::writeLambdaFile);
    }

    private void writeSourceFile(String name){
        cw.visitSource(name, null);
    }

    private void writeNestMember(String member){
        cw.visitNestMember(member);
    }

    private void writeNestHost(String host){
        cw.visitNestHost(host);
    }

    private void writeInnerClass(InnerClass innerClass){
        cw.visitInnerClass(innerClass.getName(), innerClass.getOuterName(), innerClass.getInnerName(), innerClass.getAccess());
    }

    private Method createLambdaMethod(LambdaInstruction lambdaInstruction){
        return new Method(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, lambdaInstruction.getName(),
                Type.getMethodDescriptor(lambdaInstruction.getReturnType(), lambdaInstruction.getArgumentsTypeOfLambda()), null, true, null);

    }

    private void writeLambdaFile(LambdaInstruction lambdaInstruction, int index) {
        var lambdaClass = new MyClass(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, myClass.getClassName()+"$MyLambda"+index, "java/lang/Object", null);
        var methodLambda = createLambdaMethod(lambdaInstruction);

        LambdaWriter.createInterface(lambdaClass);
        LambdaWriter.createFields(lambdaClass, lambdaInstruction);
        LambdaWriter.createConstructor(lambdaClass, lambdaInstruction);
        LambdaWriter.createLambdaFactory(lambdaClass, lambdaInstruction, myClass.getClassName(), index);
        LambdaWriter.createLambdaCalledMethod(lambdaClass, methodLambda, lambdaInstruction, myClass.getClassName());

        var lambdaWriter = new MyWriter(lambdaClass, version, this.warningObservers, this.options);
        lambdaWriter.createClass();

        lambdaWriter.writeSourceFile(myClass.getClassName()+".java");

        lambdaWriter.writeFields();
        lambdaWriter.writeConstructors();
        lambdaWriter.writeMethods();

        try {
            lambdaWriter.createFile();

            //Now write the interface file
            var interfac = new MyInterface("interface"+myClass.getClassName()+"$MyLambda"+index, "java/lang/Object",Opcodes.ACC_ABSTRACT+Opcodes.ACC_PUBLIC+Opcodes.ACC_INTERFACE, version);
            interfac.addMethod(methodLambda);
            var writer = new InterfaceWriter(interfac);
            writer.createClass();
            writer.writeMethods();
            writer.createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all fields of the current class.
     */
    @Override
    public void writeFields(){
        myClass.getFields().forEach(this::writeField);
    }

    private void writeField(Field f){
        FieldVisitor fv = cw.visitField(f.getAccess(), f.getName(), f.getDescriptor(), f.getSignature(), f.getValue());
        fv.visitEnd();
    }

    /**
     * Writes all constructors of the current class.
     */
    @Override
    public void writeConstructors(){
        myClass.getConstructors().forEach(this::writeMethod);
    }

    /**
     * Writes all methods of the current class.
     */
    @Override
    public void writeMethods(){
        myClass.getMethods().forEach(this::writeMethod);

        //Write access methods if is a inner class but no a lambda class
        if(myClass.getClassName().contains("$") && !myClass.getClassName().contains("MyLambda")){
            if(version < FieldInstruction.VERSION) {
                if (options.forceIsDemanding()){
                    myClass.getFields().stream().filter(n -> !n.getName().contains("this")).forEach(this::writeAccessMethod);
                }
                else{
                    warningObservers.forEach(o -> o.onWarningDetected("We have detected a nestMates in " + myClass.getClassName(), "nestMates"));
                }
            }
        }
    }

    private void writeAccessMethod(Field f){
        //Getter
        mw = cw.visitMethod(Opcodes.ACC_STATIC+Opcodes.ACC_PUBLIC, "accessGetter"+f.getName().toUpperCase(), "(L"+myClass.getClassName()+";)"+f.getDescriptor(), null, null);
        mw.visitVarInsn(Opcodes.ALOAD, 0);
        mw.visitFieldInsn(Opcodes.GETFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
        mw.visitInsn(UtilsWriter.getReturnInAccordingToTheType(f.getDescriptor()));
        mw.visitMaxs(0, 0);
        mw.visitEnd();
        //Setter
        mw = cw.visitMethod(Opcodes.ACC_STATIC+Opcodes.ACC_PUBLIC, "accessSetter"+f.getName().toUpperCase(), "(L"+myClass.getClassName()+";"+f.getDescriptor()+")"+f.getDescriptor(), null, null);
        mw.visitVarInsn(Opcodes.ALOAD, 0);
        mw.visitVarInsn(UtilsWriter.getLoadInAccordingToTheType(f.getDescriptor()), 1);
        mw.visitInsn(UtilsWriter.getDupOrDup2(f.getDescriptor()));
        mw.visitFieldInsn(Opcodes.PUTFIELD, myClass.getClassName(), f.getName(), f.getDescriptor());
        mw.visitInsn(UtilsWriter.getReturnInAccordingToTheType(f.getDescriptor()));
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    private void writeMethod(Method m) {
        if(m.getName().contains("$"))
            mw = cw.visitMethod(Opcodes.ACC_PUBLIC, m.getName(), m.getDescriptor(), m.getSignature(), m.getExceptions());
        else{
            mw = cw.visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), m.getSignature(), m.getExceptions());
        }
        mw.visitCode();
        m.writeAllInstructions(version, mw, myClass.getClassName());
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    /**
     * Creates a new .class file with the bytecode.
     * @return the path of the file which contain the bytecode
     * @throws IOException - if problem occurred during the creating or writing of the file.
     */
    @Override
    public String createFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(myClass.getClassName()+".class");
        fos.write(cw.toByteArray());
        fos.close();
        return myClass.getClassName()+".class";
    }

    @Override
    public List<WarningObserver> getObservers() {
        return warningObservers;
    }

    @Override
    public MyClass getMyClass() {
        return myClass;
    }

    @Override
    public MethodVisitor getMethodVisitor() {
        return mw;
    }

    @Override
    public void setMethodVisitor(MethodVisitor methodVisitor) {
        mw = methodVisitor;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public ClassWriter getClassWriter() {
        return cw;
    }
}
