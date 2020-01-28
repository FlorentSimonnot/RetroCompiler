package fr.project.instructions.features;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

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
     * Get the last index which exists.
     * @return int - the last index in the collector
     */
    public int getIndex(){return lambdas.size()-1;}

    public boolean contains(String name, String owner){
        for (Map.Entry<LambdaInstruction, Integer> entry : lambdas.entrySet()) {
            if(entry.getKey().getName().equals(name) && entry.getKey().getOwnerClass().equals(owner))
                return true;
        }
        return false;
    }

}