package fr.project.instructions.simple;

import fr.project.instructions.features.LambdaCollector;

import java.util.ArrayList;
import java.util.List;

public class MyInterface {
    private final String className;
    private final List<Method> methods;
    private final String ownerClassName;
    private final int privacy;
    private final int version;

    public MyInterface(String className, String ownerClassName, int privacy, int version) {
        this.className = className;
        this.methods = new ArrayList<>();
        this.ownerClassName = ownerClassName;
        this.privacy = privacy;
        this.version = version;
    }

    public void addMethod(Method method){
        methods.add(method);
    }

    public String getClassName() {
        return className;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public String getOwnerClassName() {
        return ownerClassName;
    }

    public int getPrivacy() {
        return privacy;
    }

    public int getVersion() {
        return version;
    }
}
