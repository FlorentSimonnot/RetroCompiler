package fr.project.detection.observers;

import java.util.HashMap;

/**
 * 
 * A Factory that associate a feature with a observer.
 * It allows to check what features you want to detect and/or write in java bytecode.
 * This class is used in the class App.
 * @author SIMONNOT Florent
 * 
 */
public class FeaturesObserverFactory {
    private final HashMap<String, FeatureObserver> register = new HashMap<>();

    /**
     * Registers a feature and its observer in the register.
     * @param feature - the name of a feature
     * @param observer - the FeatureObserver linked to the feature
     */
    public void register(String feature, FeatureObserver observer){
        this.register.putIfAbsent(feature, observer);
    }

    /**
     * Gets the Observer according to the feature.
     * @param feature - the name of a feature
     * @return the FeatureObserver asked.
     */
    FeatureObserver getObserver(String feature){
        return register.get(feature);
    }

}
