package fr.project.warningObservers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A class that allows to observe the behavior of a try-with-resources instruction.
 * @author SIMONNOT Florent
 *
 */
public class WarningTryWithResourcesObserver implements WarningObserver {
    private final List<String> warnings = new ArrayList<>();

    @Override
    public void onWarningDetected(String warning, String token) {
        if(token.equals("try-with-resources")) warnings.add(warning);
    }

    @Override
    public void showWarning() {
        if(warnings.size() == 0) return;
        System.err.println("We found " +  warnings.size() + " try-with-resources !");
        warnings.forEach(System.out::println);
    }

    @Override
    public int numberOfWarningsDetected() {
        return warnings.size();
    }
}
