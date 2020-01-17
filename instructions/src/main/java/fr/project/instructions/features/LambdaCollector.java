package fr.project.instructions.features;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A lambdas collector.
 * This class is stored as a lambdas collector of a MyClassVisitor.
 * @author SIMONNOT Florent
 *
 */
public class LambdaCollector {
    private final Map<LambdaInstruction, Integer> lambdas = new HashMap<>();

    /**
     * Adds a lambda into the collector.
     * @param lambdaInstruction - a LambdaInstruction object to add
     * @return the collector's new size
     */
    public int addLambda(LambdaInstruction lambdaInstruction){
        lambdas.put(Objects.requireNonNull(lambdaInstruction), lambdas.size());
        return lambdas.size()-1;
    }

    /**
     * Applies a function to each lambda instruction stored in the collector.
     * @param consumer - the function to apply
     */
    public void forEach(BiConsumer<? super LambdaInstruction, ? super Integer> consumer){
        lambdas.forEach(consumer);
    }

    /**
     * Tests if a lambda is in the collector.
     * @param name - a lambda's name
     * @param owner - the lambda's owner class
     * @return true if the lambda is already in the collector, false if not
     */
    public boolean lambdaAlreadyExists(String name, String owner){
        for(LambdaInstruction l : lambdas.keySet()){
            if(l.getOwnerClass().equals(owner) && l.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a lambda's index.
     * @param name - a lambda's name
     * @param owner - the lambda's owner class
     * @return the lambda's index if it is in the collector
     */
    public Integer getLambdaIndex(String name, String owner){
        for(LambdaInstruction l : lambdas.keySet()){
            if(l.getOwnerClass().equals(owner) && l.getName().equals(name)){
                return lambdas.get(l);
            }
        }
        throw new IllegalStateException();
    }

    /**
     * Gets a lambda.
     * @param name - a lambda's name
     * @param owner - the lambda's owner class
     * @return the lambda if it is in the collector
     */
    public LambdaInstruction getLambda(String name, String owner){
        for(LambdaInstruction l : lambdas.keySet()){
            if(l.getOwnerClass().equals(owner) && l.getName().equals(name)){
                return l;
            }
        }
        throw new IllegalStateException();
    }
}