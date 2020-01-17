package fr.project.detection.observers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A observer that observes the behavior of a try-with-resources instruction.
 * It allows to notify the user when a try-with-resources instruction is detected in the .class file.
 * @author SIMONNOT Florent
 *
 */
public class TryWithResourcesObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the try-with-resources feature is detected.
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("addSuppressed"))
            features.add(methodName);
    }

    /**
     * Displays the list of String corresponding of all try-with-resources detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(System.out::println);
    }
}
