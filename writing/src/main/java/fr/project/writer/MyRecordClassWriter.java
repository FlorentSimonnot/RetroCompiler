package fr.project.writer;

import fr.project.instructions.simple.Field;
import fr.project.instructions.simple.Method;
import fr.project.instructions.simple.MyClass;
import fr.project.warningObservers.WarningObserver;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.List;

public class MyRecordClassWriter implements Writer{
    private final Writer writer;

    public MyRecordClassWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void createClass() {
        writer.getObservers().forEach(o -> o.onWarningDetected("[WARNING] : " + writer.getMyClass().getClassName() + " is a record class", "record"));
        writer.createClass();
    }

    @Override
    public void writeLambdaInnerClasses() {
        writer.writeLambdaInnerClasses();
    }

    @Override
    public void writeLambdaFiles() {
        writer.writeLambdaFiles();
    }

    @Override
    public void writeFields() {
        writer.writeFields();
    }

    @Override
    public void writeConstructors() {
        writer.getMyClass().getConstructors().forEach(this::writeMethod);
    }

    private void writeConstructor(Method m){
        writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, null));
        m.writeAllInstructions(writer.getVersion(), writer.getMethodVisitor(), writer.getMyClass().getClassName());
        // this code uses a maximum of one stack element and one local variable
        writer.getMethodVisitor().visitMaxs(0, 0);
        writer.getMethodVisitor().visitEnd();
    }

    @Override
    public void writeMethods() {
        writer.getMyClass().getMethods().forEach(this::writeMethod);
    }

    private void writeMethod(Method m){
        switch(m.getName()){
            case "<init> " : writeConstructor(m); break;
            case "toString" : writeToStringMethodForRecord(m); break;
            case "equals" : writeEqualsMethodForRecord(m); break;
            case "hashCode" : writeHashCodeMethodForRecord(m); break;
            default: {
                writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));
                m.writeAllInstructions(writer.getVersion(), writer.getMethodVisitor(), writer.getMyClass().getClassName());
                // this code uses a maximum of one stack element and one local variable
                writer.getMethodVisitor().visitMaxs(0, 0);
                writer.getMethodVisitor().visitEnd();
            } break;
        }
    }

    private boolean isEqualsMethodCustom(Method m){
        return !m.haveInvokeDynamicInstructionForEqualsMethod(getMyClass());
    }

    private boolean isHashCodeMethodCustom(Method m){
        return !m.haveInvokeDynamicInstructionForHashCodeMethod(getMyClass());
    }

    private boolean isToStringMethodCustom(Method m){
        return !m.haveInvokeDynamicInstructionForToStringMethod(getMyClass());
    }

    private void writeEqualsMethodForRecord(Method m) {

        if(isEqualsMethodCustom(m)){
            writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));
            m.writeAllInstructions(writer.getVersion(), writer.getMethodVisitor(), writer.getMyClass().getClassName());
            // this code uses a maximum of one stack element and one local variable
            writer.getMethodVisitor().visitMaxs(0, 0);
            writer.getMethodVisitor().visitEnd();
            return;
        }

        writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));
        var mw = writer.getMethodVisitor();
        mw.visitCode();
        mw.visitVarInsn(Opcodes.ALOAD, 1);
        mw.visitTypeInsn(Opcodes.INSTANCEOF, writer.getMyClass().getClassName());
        var l1 = new Label();
        mw.visitJumpInsn(Opcodes.IFNE, l1);
        //False
        mw.visitInsn(Opcodes.ICONST_0);
        mw.visitInsn(Opcodes.IRETURN);

        mw.visitLabel(l1);
        mw.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

        mw.visitVarInsn(Opcodes.ALOAD, 1);
        mw.visitTypeInsn(Opcodes.CHECKCAST, writer.getMyClass().getClassName());
        mw.visitVarInsn(Opcodes.ASTORE, 2);
        var notEqualLabel = new Label();
        writer.getMyClass().getFields().forEach(f -> {
            mw.visitVarInsn(Opcodes.ALOAD, 2);
            mw.visitFieldInsn(Opcodes.GETFIELD, writer.getMyClass().getClassName(), f.getName(), f.getDescriptor());
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitFieldInsn(Opcodes.GETFIELD, writer.getMyClass().getClassName(), f.getName(), f.getDescriptor());
            writeCompareNotEqual(f.getDescriptor(), mw, notEqualLabel);
        });
        var returnLabel = new Label();
        //Here, all comparisons are true
        mw.visitInsn(Opcodes.ICONST_1);
        mw.visitJumpInsn(Opcodes.GOTO, returnLabel);
        //Here we visit the label because one of fields are not equals
        mw.visitLabel(notEqualLabel);
        //mw.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mw.visitFrame(Opcodes.F_APPEND, 1, new Object[]{writer.getMyClass().getClassName()}, 0, null);
        mw.visitInsn(Opcodes.ICONST_0);
        //Return the result of comparisons
        mw.visitLabel(returnLabel);
        //mw.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mw.visitFrame(Opcodes.F_SAME1, 0, new Object[]{writer.getMyClass().getClassName()}, 1, new Object[]{1});
        mw.visitInsn(Opcodes.IRETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    private void writeCompareNotEqual(String type, MethodVisitor mv, Label label){
        if(type.startsWith("[")){
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "equals", "("+type+type+")Z", false);
            mv.visitJumpInsn(Opcodes.IFEQ, label);
            return;
        }
        switch(type){
            case "I" :
            case "Z" :
            case "B" :
                mv.visitJumpInsn(Opcodes.IF_ICMPNE, label); break;
            case "F" : mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFNE, label); break;
            case "J" : mv.visitInsn(Opcodes.LCMP); mv.visitJumpInsn(Opcodes.IFNE, label); break;
            case "D" : mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFNE, label); break;
            default: mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type.replace("L", "").replace(";", ""), "equals", "(Ljava/lang/Object;)Z", false); mv.visitJumpInsn(Opcodes.IFEQ, label); break;
        }
    }

    private void writeHashCodeMethodForRecord(Method m){

        if(isHashCodeMethodCustom(m)){
            writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));
            m.writeAllInstructions(writer.getVersion(), writer.getMethodVisitor(), writer.getMyClass().getClassName());
            // this code uses a maximum of one stack element and one local variable
            writer.getMethodVisitor().visitMaxs(0, 0);
            writer.getMethodVisitor().visitEnd();
            return;
        }

        writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));
        var mw = writer.getMethodVisitor();
        mw.visitCode();
        mw.visitVarInsn(Opcodes.BIPUSH, 7);
        mw.visitVarInsn(Opcodes.ISTORE, 1);
        writer.getMyClass().getFields().forEach(f -> {
            mw.visitVarInsn(Opcodes.BIPUSH, 31);
            mw.visitVarInsn(Opcodes.ILOAD, 1);
            mw.visitInsn(Opcodes.IMUL);
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitFieldInsn(Opcodes.GETFIELD, writer.getMyClass().getClassName(), f.getName(), f.getDescriptor());
            writeHashCodeInAccordingWithType(f.getDescriptor(), mw, f);
            mw.visitVarInsn(Opcodes.ISTORE, 1);
        });
        mw.visitVarInsn(Opcodes.ILOAD, 1);
        mw.visitInsn(Opcodes.IRETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }

    private void writeHashCodeInAccordingWithType(String type, MethodVisitor mv, Field f){
        //This is an Array
        if(type.startsWith("[")){
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "hashCode", "("+type+")I", false);
            mv.visitInsn(Opcodes.IADD);
            return;
        }
        switch (type){
            case "B" :
            case "I" : mv.visitInsn(Opcodes.IADD); break;
            case "Z" : {
                var labelBool = new Label();
                mv.visitJumpInsn(Opcodes.IFEQ, labelBool);
                mv.visitInsn(Opcodes.ICONST_0);
                var labelAddBoolHashCode = new Label();
                mv.visitJumpInsn(Opcodes.GOTO, labelAddBoolHashCode);
                mv.visitLabel(labelBool);
                mv.visitFrame(Opcodes.F_FULL, 2,  new Object[]{"RecordClassTestEquals", 1}, 1, new Object[]{1});
                mv.visitInsn(Opcodes.ICONST_1);
                mv.visitLabel(labelAddBoolHashCode);
                mv.visitFrame(Opcodes.F_FULL, 2,  new Object[]{"RecordClassTestEquals", 1}, 2, new Object[]{1, 1});
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            case "F" : {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "floatToIntBits", "("+type+")I", false );
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            case "J" : {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, writer.getMyClass().getClassName(), f.getName(), f.getDescriptor());
                mv.visitVarInsn(Opcodes.BIPUSH, 32);
                mv.visitInsn(Opcodes.LUSHR);
                mv.visitInsn(Opcodes.LXOR);
                mv.visitInsn(Opcodes.L2I);
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            case "D" :{
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "doubleToLongBits", "("+type+")J", false );
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, writer.getMyClass().getClassName(), f.getName(), f.getDescriptor());
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "doubleToLongBits", "("+type+")J", false );
                mv.visitVarInsn(Opcodes.BIPUSH, 32);
                mv.visitInsn(Opcodes.LUSHR);
                mv.visitInsn(Opcodes.LXOR);
                mv.visitInsn(Opcodes.L2I);
                mv.visitInsn(Opcodes.IADD);
                break;
            }
            default: mv.visitMethodInsn(Opcodes.INVOKESTATIC, type.replace("L", "").replace(";", ""), "hashCode", "(Ljava/lang/Object;)I", false ); mv.visitInsn(Opcodes.IADD); break;
        }
    }

    private void writeToStringMethodForRecord(Method m){

        if(isToStringMethodCustom(m)){
            writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));
            m.writeAllInstructions(writer.getVersion(), writer.getMethodVisitor(), writer.getMyClass().getClassName());
            // this code uses a maximum of one stack element and one local variable
            writer.getMethodVisitor().visitMaxs(0, 0);
            writer.getMethodVisitor().visitEnd();
            return;
        }

        writer.setMethodVisitor(writer.getClassWriter().visitMethod(m.getAccess(), m.getName(), m.getDescriptor(), null, m.getExceptions()));

        var mw = writer.getMethodVisitor();
        mw.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mw.visitInsn(Opcodes.DUP);

        mw.visitLdcInsn(writer.getMyClass().getClassName()+"[");

        mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mw.visitVarInsn(Opcodes.ASTORE, 1);
        mw.visitVarInsn(Opcodes.ALOAD, 1);

        var numberOfFields = writer.getMyClass().getFields().size();
        var count = 0;
        for(Field f : writer.getMyClass().getFields()){
            mw.visitLdcInsn(f.getName()+"=");
            mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            var descriptor = "";

            if(f.getDescriptor().equals("B")){
                descriptor = "I";
            }
            else if(f.getDescriptor().startsWith("[") || f.getDescriptor().startsWith("L")){
                descriptor = "Ljava/lang/Object;";
            }
            else{
                descriptor = f.getDescriptor();
            }

            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitFieldInsn(Opcodes.GETFIELD, writer.getMyClass().getClassName(), f.getName(), f.getDescriptor());
            mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "("+descriptor+")Ljava/lang/StringBuilder;", false);

            if(count < numberOfFields-1) {
                mw.visitLdcInsn(", ");
                mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
            count++;
        }

        mw.visitLdcInsn("]");
        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        mw.visitInsn(Opcodes.POP);
        mw.visitVarInsn(Opcodes.ALOAD, 1);

        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);

        mw.visitInsn(Opcodes.ARETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
    }


    @Override
    public String createFile() throws IOException {
        return writer.createFile();
    }

    @Override
    public List<WarningObserver> getObservers() {
        return writer.getObservers();
    }

    @Override
    public MyClass getMyClass() {
        return writer.getMyClass();
    }

    @Override
    public MethodVisitor getMethodVisitor() {
        return writer.getMethodVisitor();
    }

    @Override
    public void setMethodVisitor(MethodVisitor methodVisitor) {
        writer.setMethodVisitor(methodVisitor);
    }

    @Override
    public int getVersion() {
        return writer.getVersion();
    }

    @Override
    public ClassWriter getClassWriter() {
        return writer.getClassWriter();
    }
}
