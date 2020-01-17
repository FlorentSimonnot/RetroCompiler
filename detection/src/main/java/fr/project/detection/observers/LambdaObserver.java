package fr.project.detection.observers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A observer that observes the behavior of a lambda instruction.
 * It allows to notify the user when a lambda instruction is detected in the .class file.
 * @author SIMONNOT Florent
 * 
 */
public class LambdaObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the lambda feature is detected.
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("lambda")) {
        	features.add(methodName);
        }
        
    }

    /**
     * Displays the list of String corresponding of all lambda detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(System.out::println);
    }
}
