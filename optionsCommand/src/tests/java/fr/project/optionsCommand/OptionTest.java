package fr.project.optionsCommand;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    @Test
    void shouldReturnTheGoodOptionEnum(){
        assertAll(
                () -> {
                    assertEquals(Option.OptionEnum.INFO, new Option(Option.OptionEnum.INFO).getOption());
                },
                () -> {
                    assertEquals(Option.OptionEnum.HELP, new Option(Option.OptionEnum.HELP).getOption());
                },
                () -> {
                    assertEquals(Option.OptionEnum.TARGET, new Option(Option.OptionEnum.TARGET).getOption());
                },
                () -> {
                    assertEquals(Option.OptionEnum.FEATURES, new Option(Option.OptionEnum.FEATURES).getOption());
                }
        );
    }

    @Test
    void shouldReturnTheGoodToStringWhenOptionHaveNotArguments(){
        assertAll(
                () -> {
                    assertEquals("INFO", new Option(Option.OptionEnum.INFO).toString());
                },
                () -> {
                    assertEquals("HELP", new Option(Option.OptionEnum.HELP).toString());
                },
                () -> {
                    assertEquals("TARGET", new Option(Option.OptionEnum.TARGET).toString());
                },
                () -> {
                    assertEquals("FEATURES", new Option(Option.OptionEnum.FEATURES).toString());
                }
        );
    }

    @Test
    void shouldReturnTheGoodToStringWhenOptionHaveArguments(){
        var optionTargetWithArgs = new Option(Option.OptionEnum.TARGET);
        optionTargetWithArgs.setArgs("8");
        var optionFeaturesWithArgs = new Option(Option.OptionEnum.FEATURES);
        optionFeaturesWithArgs.setArgs("[try-with-resources, lambda]");
        assertAll(
                () -> {
                    assertEquals("INFO", new Option(Option.OptionEnum.INFO).toString());
                },
                () -> {
                    assertEquals("HELP", new Option(Option.OptionEnum.HELP).toString());
                },
                () -> {
                    assertEquals("TARGET 8", optionTargetWithArgs.toString());
                },
                () -> {
                    assertEquals("FEATURES [try-with-resources, lambda]", optionFeaturesWithArgs.toString());
                }
        );
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenWeSetArgumentToAnOptionWhichNotTakeArgument(){
        var optionInfo = new Option(Option.OptionEnum.INFO);
        var optionHelp = new Option(Option.OptionEnum.HELP);
        assertAll(
                () -> {
                    assertThrows(IllegalStateException.class, () -> {
                       optionHelp.setArgs("args");
                    });
                },
                () -> {
                    assertThrows(IllegalStateException.class, () -> {
                       optionInfo.setArgs("args");
                    });
                }
        );
    }
}