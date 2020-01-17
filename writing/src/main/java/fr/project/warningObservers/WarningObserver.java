package fr.project.warningObservers;

/**
 * An interface that represents an observer on features.
 * @author SIMONNOT Florent
 *
 */
public interface WarningObserver {

	/**
	 * Notifies the user of a warning detected.
	 * @param warning - the warning message
	 * @param token - the warning's token
	 */
    void onWarningDetected(String warning, String token);
    void showWarning();
    int numberOfWarningsDetected();

}
