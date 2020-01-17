package fr.project.warningObservers;

import java.util.HashMap;

/**
 * 
 * A Factory that associate a warning as String with a WarningObserver.
 * @author SIMONNOT Florent
 *
 */
public class WarningObserverFactory {
    private final HashMap<String, WarningObserver> register = new HashMap<>();

    /**
     * Registers a warning and its observer in the register.
     * @param feature - the name of the warning
     * @param observer - the WarningObserver linked to the feature
     */
    public void register(String feature, WarningObserver observer){
        this.register.putIfAbsent(feature, observer);
    }

    /**
     * Gets the Observer according to the warning.
     * @param feature - a fr.project.instructions.simple String
     * @return the WarningObserver asked.
     */
    WarningObserver getObserver(String feature){
        return register.get(feature);
    }

}
