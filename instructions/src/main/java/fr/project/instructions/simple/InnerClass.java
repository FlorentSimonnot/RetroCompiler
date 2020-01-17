package fr.project.instructions.simple;

/**
 * A class that represents an inner class of a .class file.
 * @author CHU Jonathan
 *
 */
public class InnerClass {
    private final String name;
    private final String outerName;
    private final String innerName;
    private final int access;

    /**
     * Creates a new InnerClass.
     * @param name - the inner class's name
     * @param outerName - the outer class's name
     * @param innerName - the inner class's inner class
     * @param access - the inner class's access
     */
    public InnerClass(String name, String outerName, String innerName, int access) {
        this.name = name;
        this.outerName = outerName;
        this.innerName = innerName;
        this.access = access;
    }

    public String getName(){return name;}
    public String getOuterName(){return outerName;}
    public String getInnerName(){return innerName;}
    public int getAccess(){return access;}
}