package fr.project.detection.observers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A observer that observes the behavior of a record.
 * It allows to notify the user when a record feature is detected in the .class file.
 * @author SIMONNOT Florent
 *
 */
public class RecordObserver implements FeatureObserver {
    private final List<String> features = new ArrayList<>();

    /**
     * Adds the text format into the list of String when the record feature is detected.
     * 
     */
    @Override
    public void onFeatureDetected(String methodName, String featureName) {
        if(featureName.equals("record"))
            features.add(methodName);
    }

    /**
     * Displays the list of String corresponding of all records detected into a .class file.
     */
    @Override
    public void showFeatures() {
        features.forEach(System.out::println);
    }
}
