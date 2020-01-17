package fr.project.detection.observers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * A observer that observes the behavior of a concatenation instruction.
 * It allows to notify the user when a concatenation instruction is detected in the .class file.
 * @author SIMONNOT Florent
 *
 */
public class ConcatenationObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the concatenation feature is detected.
     * 
     */
    @Override
    public void onFeatureDetected(String message, String featureName) {
        Objects.requireNonNull(featureName);
        if(featureName.equals("concatenation"))
            features.add(Objects.requireNonNull(message));
    }

    /**
     * Displays the list of String corresponding of all concatenations detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(System.out::println);
    }
}
