package fr.project.optionsCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 
 * A class that provides tools to verify all the options given by the user when you run the project Retro.
 * @author SIMONNOT Florent
 *
 */
public class OptionUtils {
    private static final String[] featuresArray = new String[]{"try-with-resources", "nestMember", "lambda", "record", "concatenation"};
    private static final List<String> featuresList = new ArrayList<>(Arrays.asList(featuresArray));

    /**
     * Gets the arguments of the options [--target] or [--features].
     * @param optionEnum - an OptionEnum that can be TARGET or FEATURES
     * @param argument - the arguments of the option given
     * @return all the arguments of an option given.
     */
    public static String checkArgument(Option.OptionEnum optionEnum, String argument){
        Objects.requireNonNull(optionEnum, argument);
        switch(optionEnum){
            case TARGET: return checkVersionNumber(argument);
            case FEATURES: return checkListOfFeatures(argument);
            default: throw new IllegalStateException("This option can't take this argument");
        }
    }

    /**
     * Tests if an option is followed by arguments.
     * @param option - an Option
     * @return true if the option given is followed by arguments, false if not.
     */
    static boolean lastOptionWaitingArgument(Option option){
        if(option.getOption() == Option.OptionEnum.TARGET || option.getOption() == Option.OptionEnum.FEATURES){
            return option.argsIsEmpty();
        }
        return false;
    }

    private static String checkVersionNumber(String arg){
        var version = Integer.parseInt(Objects.requireNonNull(arg));
        if(version > 4 && version < 14){
            return arg;
        }
        throw new IllegalArgumentException("This argument is wrong !");
    }

    private static String checkListOfFeatures(String arg){
        Objects.requireNonNull(arg);
        var listTrim = arg.replaceAll("\\s", "");
        var features = listTrim.subSequence(1, listTrim.length()-1).toString().split(",");
        if(features.length > 0){
            for(String feature : features){
                if(!featuresList.contains(feature)){
                    throw new IllegalArgumentException(feature + " is invalid !");
                }
            }
            return listTrim;
        }
        throw new IllegalArgumentException("Arguments are invalids !");
    }

}
