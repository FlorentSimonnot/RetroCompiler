package fr.project.detection.observers;

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
public class FeaturesManager {

    private FeaturesManager(){}

    /**
     * Creates a list of FeatureObserver according to the features asked when the executable is running.
     * @param args - the list of features asked
     * @param factory - a factory of FeaturesObserver
     * @return the list of FeaturesObservers asked.
     */
    public static List<FeatureObserver> createObservers(String args, FeaturesObserverFactory factory){
        List<FeatureObserver> observers = new ArrayList<>();
        if (!args.equals("")) {
        	var splitArgs = args.subSequence(1, args.length()-1).toString();
        	var argsArray = splitArgs.split(",");
        	Arrays.stream(argsArray).map(String::strip).forEach(arg -> observers.add(factory.getObserver(arg)));
        }
        else {
        	observers.add(new LambdaObserver());
        	observers.add(new ConcatenationObserver());
        	observers.add(new NestMemberObserver());
        	observers.add(new RecordObserver());
        	observers.add(new TryWithResourcesObserver());
        }
        return observers;
    }


}
