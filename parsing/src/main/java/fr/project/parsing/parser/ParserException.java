package fr.project.parsing.parser;

/**
 * 
 * A class that allows to throw exceptions when you parse your files for the project Retro.
 * @author SIMONNOT Florent
 *
 */
public class ParserException extends Exception {

	/**
	 * Creates a new ParserException.
	 */
    public ParserException(){
        super();
    }

    /**
     * Creates a new ParserException.
     * @param s - the error message you want to display
     */
    public ParserException(String s){
        super(s);
    }

}
