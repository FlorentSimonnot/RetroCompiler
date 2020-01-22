package fr.project.instructions.simple;

import fr.project.instructions.features.LambdaCollector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 
 * A class that represents a class of a .class file.
 * It contains methods that allow to dectect and write fields and methods of the class.
 * @author CHU Jonathan
 *
 */
public class MyClass {
    private final String className;
    private String sourceName;
    private final List<Field> fields;
    private final List<Method> methods;
    private final List<InnerClass> innerClasses;
    private final List<String> nestMembers = new ArrayList<>();
    private final List<String> nestHosts = new ArrayList<>();
    private LambdaCollector lambdaCollector = new LambdaCollector();
    private final String ownerClassName;
    private final int privacy;
    private int lineNumber;
    private String[] interfaces;

    /**
     * Creates a new MyClass.
     * @param privacy - the class's visibility
     * @param className - the class's name
     * @param ownerClassName - the owner class's name
     * @param interfaces - the interface the class implements
     */
    public MyClass(int privacy, String className, String ownerClassName, String[] interfaces){
        if(privacy < 0) throw new IllegalArgumentException("Privacy must be positive");
        this.privacy = privacy;
        this.className = Objects.requireNonNull(className);
        this.ownerClassName = Objects.requireNonNull(ownerClassName);
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.interfaces = interfaces;
    }

    /* *************************************** */
    /*                GETTERS                  */
    /* *************************************** */

    /**
     * Gets the class's name.
     * @return the class's name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the class's fields.
     * @return the class's fields
     */
    public List<Field> getFields() {
        return fields;
    }

    public void addInterface(String name){
        Objects.requireNonNull(name);
        if(this.interfaces == null){
            this.interfaces = new String[1];
            this.interfaces[0] = name;
        }else {
            var copy = Arrays.copyOf(interfaces, interfaces.length + 1);
            copy[copy.length - 1] = name;
            this.interfaces = Arrays.copyOf(copy, copy.length);
        }
    }

    public void addNestMember(String nestMate){
        nestMembers.add(nestMate);}

    public void addNestHost(String nestHost){nestHosts.add(nestHost);}

    public List<String> getNestMembers(){return nestMembers;}

    public List<String> getNestHosts(){return nestHosts;}

    public void addInnerClass(InnerClass innerClass){
        innerClasses.add(innerClass);
    }

    /**
     * Gets the class's constructors.
     * @return the class's constructors
     */
    public List<Method> getConstructors(){
        var constructors = new ArrayList<Method>();
        methods.forEach(m -> {
            if(m.getName().equals("<init>")){
                constructors.add(m);
            }
        });
        return constructors;
    }

    /**
     * Gets the class's methods that are not constructors.
     * @return the class's methods that are not constructors.
     */
    public List<Method> getMethods(){
        var notConstructors = new ArrayList<Method>();
        methods.forEach(m -> {
            if(!m.getName().equals("<init>")){
                notConstructors.add(m);
            }
        });
        return notConstructors;
    }

    /**
     * Gets the class's visibility.
     * @return the class's visibility
     */
    public LambdaCollector getLambdaCollector(){return lambdaCollector;}

    /**
     * Gets the class's inner classes.
     * @return the list of InnerClass of the .class file.
     */
    public List<InnerClass> getInnerClasses(){
        return new ArrayList<>(innerClasses);
    }

    /**
     * Gets the privacy of the class.
     * @return the class's privacy
     */
    public int getPrivacy() {
        return privacy;
    }

    /**
     * Gets the interfaces the class implements.
     * @return the interfaces the class implements
     */
    public String[] getInterfaces(){return interfaces;}

    /**
     * Sets the .class file name.
     * @param sourceName - the target name of the .class file
     */
    public void setSourceName(String sourceName){this.sourceName = Objects.requireNonNull(sourceName);}

    /**
     * Sets the line number of the class.
     * @param lineNumber - the new line number
     */
    public void setLineNumber(int lineNumber){
        if(lineNumber < 0) throw new IllegalArgumentException("Line number must be positive");
        this.lineNumber = lineNumber;
    }

    /**
     * Sets the lambdaCollector of the class.
     * @param lambdaCollector - a LambdaCollector object
     */
    public void setLambdaCollector(LambdaCollector lambdaCollector){
        this.lambdaCollector = Objects.requireNonNull(lambdaCollector);
    }

    /**
     * Gets the class's line number.
     * @return the class's line number
     */
    public int getLineNumber(){return lineNumber;}

    /**
     * Adds a field into the field fields.
     * @param field - a new Field object
     */
    public void addField(Field field){
        fields.add(field);
    }

    /**
     * Adds a method into the field methods.
     * @param method - a Method object
     */
    public void addMethod(Method method){
        methods.add(method);
    }

    /**
     * Gets all class's methods.
     * @return all class's methods
     */
    public List<Method> getAllMethods() {
        return methods;
    }

    /**
     * Gets .class file's name.
     * @return .class file's name
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * Tests if the class's owner class is a record.
     * @return true if the class's owner class is a record, false if not
     */
    public boolean isRecordClass(){
        return ownerClassName.equals("java/lang/Record");
    }
}
