package fr.project.optionsCommand;

import fr.project.optionsCommand.Option.OptionEnum;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 
 * A class that describes all the options given into the run command.
 * This class is used when you run the project Retro.
 * It can contains the options: [--help], [--info], [--target] or [--features].
 * @author SIMONNOT Florent
 *
 */
public class Option {
    private final OptionEnum option;
    private String args = "";

    /**
     * All the options available with the project Retro.
     *
     */
    public enum OptionEnum{
        HELP,
        INFO,
        TARGET,
        FEATURES,
        NULL,
        FORCE;
    }

    /**
     * Creates a new Option.
     * @param option - the OptionEnum corresponding to the option asked
     */
    public Option(OptionEnum option){
        this.option = option;
    }
    
    public static void showHelp() throws IOException {
        var path = Paths.get("optionsCommand/src/main/resources/help.txt");
        try(var reader = Files.newBufferedReader(path)){
            reader.lines().forEach(System.out::println);
        }catch(IOException e){
            throw new IOException("File help can't be read");
        }
    }

    /**
     * Sets the arguments of the project Retro.
     * @param args - a String corresponding to the arguments
     */
    public void setArgs(String args){
        if(option == OptionEnum.TARGET || option == OptionEnum.FEATURES)
            this.args = args;
        else{
            throw new IllegalStateException("This option doesn't take an argument");
        }
    }

    /**
     * Gets the option of the project Retro.
     * @return the actual option of the project Retro
     */
    public OptionEnum getOption(){
        return option;
    }

    /**
     * Tests if the arguments are empty.
     * @return true if the argument are empty, false if not
     */
    boolean argsIsEmpty(){
        return args.isEmpty();
    }

    /**
     * Gets all the arguments of the project Retro.
     * @return a String corresponding to the arguments.
     */
    public String getArgs(){
        return args;
    }

    @Override
    public String toString(){
        var space = "";
        if(args.length() > 0)
            space = " " + args;
        return option.name() + space;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Option)) return false;
        var opt = (Option) obj;
        return opt.option == this.option;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
