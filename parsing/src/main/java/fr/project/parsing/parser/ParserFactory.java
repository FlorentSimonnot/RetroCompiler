package fr.project.parsing.parser;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 
 * A Factory that allows to create a parser for directory, .class file or .jar file.
 * @author SIMONNOT Florent
 * 
 */
public class ParserFactory {

    /**
     * Creates a parser according to the type of file
     * @param path - the path of the file
     * @return FileParserInterface - a parser according to the type of file
     * @throws IOException - if we can't read the file
     */
    /*
    * Change because probeContentType doesn't work on mac.
    * */
    public static FileParserInterface createParser(Path path) throws IOException {
        if(Files.isDirectory(Objects.requireNonNull(path))){
            return new DirectoryParser();
        }
        else if(Files.isRegularFile(path)){
            try(FileInputStream fileInputStream = new FileInputStream(path.toFile());
                InputStream is = new BufferedInputStream(fileInputStream)
            ){
                var mimeType = URLConnection.guessContentTypeFromStream(is);
                if(mimeType == null)
                    throw new IllegalArgumentException();
                //Doesn't work on mac os
                //var probContentType = Files.probeContentType(path);
                if(mimeType.equals("application/java-vm")){
                    return new FileClassParser();
                }
                else{
                    throw new IllegalArgumentException();
                }
            }
        }
        else{
            System.out.println(path);
            if(path.toString().endsWith(".jar")){
                return new JarParser();
            }
            throw new IllegalArgumentException("Not a regular file");
        }
    }
}
