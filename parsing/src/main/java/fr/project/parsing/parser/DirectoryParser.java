package fr.project.parsing.parser;

import fr.project.parsing.files.DirectoryFile;
import fr.project.parsing.files.FilesCollector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 
 * A class that allows to parse a directory.
 * The main goal of this class is to create a list of .class files contained by the directory.
 * @author SIMONNOT Florent
 */
public class DirectoryParser implements FileParserInterface {

	@Override
	/**
	 * Collects all the .class files of a directory.
	 * @param name - the name of the directory you want to parse
	 * @return the FilesCollector of all .class files in the directory given
	 */
	public FilesCollector parseMyFile(String name) throws IOException {
		return listFilesForFolder(name);
	}

	private FilesCollector listFilesForFolder(String name) throws IOException {
		if(Objects.requireNonNull(name).contains("."))
			throw new IllegalArgumentException("We can't accept this type of file");
		var collector = new FilesCollector();
		var folder = Files.newDirectoryStream(Path.of(name));
		for (var path : folder) {
			if (path.getFileName().toString().endsWith(".class")) {
				collector.addFile(new DirectoryFile(path.getFileName().toString(), path.toString()));
			}
		}
		return collector;
	}
}
