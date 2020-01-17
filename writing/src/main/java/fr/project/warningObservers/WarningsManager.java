package fr.project.warningObservers;

import fr.project.optionsCommand.Options;

import java.net.SocketOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * A class that allows to create a list of observers for the features.
 * This class will be used by the ClassVisitor and the MethodVisitor.
 * @author SIMONNOT Florent
 *
 */
public class WarningsManager {

    private WarningsManager(){}

    /**
     * Creates a list of WarningsObserver according to the warning asked when the executable is running.
     * @param options - the list of options asked
     * @param factory - a factory of WarningObserver
     * @return the list of WarningsObserver asked.
     */
    public static List<WarningObserver> createObservers(Options options, WarningObserverFactory factory){
        List<WarningObserver> observers = new ArrayList<>();
        if(!options.forceIsDemanding()){
            observers.add(new WarningConcatenationObserver());
            observers.add(new WarningNestMemberObserver());
            observers.add(new WarningRecordObserver());
            observers.add(new WarningTryWithResourcesObserver());
            observers.add(new WarningLambdaObserver());
        }
        return observers;
    }

}
