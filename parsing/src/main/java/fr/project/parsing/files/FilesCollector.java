package fr.project.parsing.files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * 
 * A class that represents a list of FileInterface.
 * All FileInterface are used when you want to visit those files with a ClassVisitor or a MethodVisitor.
 * @author SIMONNOT Florent
 *
 */
public class FilesCollector {
	private final List<FileInterface> collector = new ArrayList<>();

	/**
	 * Adds a FileInterface into the list collector.
	 * @param fileClass - the fileClass we want to add in the collector
	 * @return true (as specified by Collection.add(E))
	 * @throws UnsupportedOperationException - if the add operation is not supported by this list
	 * @throws ClassCastException - if the class of the specified element prevents it from being added to this list
	 * @throws NullPointerException - if the specified element is null and this list does not permit null elements
	 * @throws IllegalArgumentException - if some property of this element prevents it from being added to this list
	 */
	public boolean addFile(FileInterface fileClass){
		return collector.add(fileClass);
	}

	/**
	 * Gets the java version for each file in the collector
	 * @return a string
	 */
	public String getVersions(){
		if(collector.size() == 0)
			return "No files in the collector";
		var joiner = new StringJoiner("\n");
		collector.forEach(f ->{
			try {
				joiner.add(f.getName()+" - Java " + f.getVersion());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return joiner.toString();
	}

	/**
	 * Computes the size of the collector.
	 * @return the size of the collector.
	 */
	public int getSize(){
		return collector.size();
	}

	/**
	 * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
	 * @param action - The action to be performed for each element
	 * @throws NullPointerException - if the specified element is null
	 */
	public void forEach(Consumer<? super FileInterface> action){
		collector.forEach(action);
	}

	/**
	 * Gets the FileInterface according to an index
	 * @param index - the index you want to have the FileInterface
	 * @return a FileInterface corresponding to your index in the collector
	 */
	public FileInterface get(int index){
		return collector.get(index);
	}

	/**
	 * Tests if the collector is empty
	 * @return true - if the collector is empty
	 */
	public boolean isEmpty(){
		return collector.size() == 0;
	}
}
