package fr.project.instructions.simple;


import java.util.Objects;

/**
 * 
 * A class that allows to represent a simple field of a .class file.
 * It is stored as a Field of a MyClass. And this MyClass object is used into a MyClassVisitor.
 * @author CHU Jonathan
 *
 */
public class Field {
    private final int access;
    private final String name;
    private final String descriptor;
    private final String signature;
    private final Object value;

    /**
     * Creates a new Field.
     * @param access - the visibility of the field
     * @param name - the name of the field
     * @param descriptor - the type of the field
     * @param signature - the signature of the field
     * @param value - the value of the field
     */
    public Field(int access, String name, String descriptor, String signature, Object value){
        if(access < 0) throw new IllegalArgumentException("Access must be positive");
        this.access = access;
        this.name = Objects.requireNonNull(name);
        this.descriptor = Objects.requireNonNull(descriptor);
        this.signature = signature;
        this.value = value;
    }

    /**
     * Gets the access of the Field.
     * @return the access of the Field
     */
    public int getAccess() {
        return access;
    }

    /**
     * Gets the name of the Field.
     * @return the name of the Field
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the descriptor of the Field.
     * @return the descriptor of the Field
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Gets the signature of the Field.
     * @return the signature of the Field
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Gets the value of the Field.
     * @return the value of the Field
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return access + " " + name + " " + descriptor + " " + signature + " " + value;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Field))
            return false;
        var field = (Field) obj;
        return name.equals(field.name) && access == field.access && descriptor.equals(field.descriptor)
                && signature.equals(field.signature) && value == field.value;
    }
}
