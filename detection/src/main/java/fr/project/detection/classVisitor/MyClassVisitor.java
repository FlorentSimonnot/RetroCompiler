package fr.project.detection.classVisitor;

import fr.project.detection.methodVisitor.MyMethodVisitor;
import fr.project.instructions.features.LambdaCollector;
import fr.project.instructions.simple.Field;
import fr.project.instructions.simple.InnerClass;
import fr.project.instructions.simple.Method;
import fr.project.instructions.simple.MyClass;
import fr.project.detection.observers.FeatureObserver;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 
 * A visitor to visit a .class file.
 * It contains observers that can react when the visitor detects some features.
 * It also contains a list of nest-mates of the class and store them into a field.
 * A MyClassVisitor object is linked to a .class file and cannot be linked to another .class file when the project Retro is running.
 * @author SIMONNOT Florent
 *
 */
public class MyClassVisitor extends ClassVisitor {
    private final List<FeatureObserver> observers;
    private final List<String> nestMates = new ArrayList<>();
    private final LambdaCollector lambdaCollector = new LambdaCollector();
    private MyClass myClass;

    /**
     * Creates a new MyClassVisitor.
     * @param api - the version of the ASM api
     * @param cv - a ClassVisitor you want to link with
     * @param observers - a list of FeatureObserver you want to link with
     */
    public MyClassVisitor(int api, ClassVisitor cv, List<FeatureObserver> observers) {
        super(api, cv);
        this.observers = observers;
    }

    /**
     * Gets the MyClass of the visitor.
     * @return the MyClass of the visitor.
     */
    public MyClass getMyClass(){return myClass;}

    /**
     * Visits the header of the .class file.
     * Tests if the class is a record class.
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        myClass = new MyClass(access, name, superName, interfaces);
        if(myClass.isRecordClass()){
            observers.forEach(o -> o.onFeatureDetected(name + " is a record class", "record"));
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        myClass.addInnerClass(new InnerClass(name, outerName, innerName, access));
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        return super.visitModule(name, access, version);
    }

    /**
     * Visits the source of the class.
     * Sets the .class file's name.
     */
    @Override
    public void visitSource(String source, String debug) {
        myClass.setSourceName(source);
        super.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    /**
     * Visits the nest host class of the .class file
     * Notifies the observers that a nest mate feature is detected.
     */
    @Override
    public void visitNestHost(String nestHost) {
        observers.forEach(o -> {
            o.onFeatureDetected("NESTMATES at " + myClass.getClassName() + " nestmate of " + nestHost
                    , "nestMember");
        });
        myClass.addNestHost(nestHost);
        super.visitNestHost(nestHost);
    }

    /**
     * Visits a member of the nest.
     * Adds a new nest-member into the field nestMates.
     */
    @Override
    public void visitNestMember(String nestMember) {
        nestMates.add(nestMember);
        myClass.addNestMember(nestMember);
        super.visitNestMember(nestMember);
    }

    /**
     * Visits a field of the .class file.
     * Adds a new Field object into the field myClass.
     */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        myClass.addField(new Field(access, name, descriptor, signature, value));
        return super.visitField(access, name, descriptor, signature, value);
    }

    /**
     * Visits a method of the .class file.
     * Returns a custom MethodVisitor object (MyMethodVisitor) that contains methods that allow to detect features and write them.
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv;
        mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null) {
            mv = new MyMethodVisitor(mv, observers, myClass.getAllMethods(), lambdaCollector, new Method(access, name, descriptor, signature, false, exceptions), myClass, exceptions);
        }
        return mv;
    }

    /**
     * Visits the end of the .class file.
     * Displays all the nest-hosts detected in the .class file.
     */
    @Override
    public void visitEnd() {
        if(nestMates.size() > 0)
            addNestHost();
        myClass.setLambdaCollector(lambdaCollector);
        super.visitEnd();
    }

    private void addNestHost(){
        var joiner = new StringJoiner(", ");
        for(String s : nestMates){
            joiner.add(s);
        }
        observers.forEach(o -> {
            o.onFeatureDetected("NESTMATES at " + myClass.getClassName() + " nest host of " + myClass.getClassName() + " members [" + joiner.toString() + "]"
                    , "nestMember");
        });
    }
    
}
