package fr.project.parsing.parser;

import java.io.IOException;

import fr.project.parsing.files.FilesCollector;

/**
 * 
 * An interface which its goal is to parse a file.
 * @author SIMONNOT Florent
 *
 */
public interface FileParserInterface {

	/**
	 * Collects all type of file from a file given.
	 * @param name - the name of the file you want to parse
	 * @return the FilesCollector of all type of file of the file given
	 * @throws ParserException - if a file cannot be parsed
	 * @throws IOException - if a file cannot be opened
	 */
    FilesCollector parseMyFile(String name) throws ParserException, IOException;

}
