package fr.project.optionsCommand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionFactoryTest {
    private final OptionFactory optionFactory = new OptionFactory();

    @BeforeEach
    void setUp() {
        optionFactory.register("--help", new Option(Option.OptionEnum.HELP));
        optionFactory.register("--info", new Option(Option.OptionEnum.INFO));
        optionFactory.register("--target", new Option(Option.OptionEnum.TARGET));
        optionFactory.register("--features", new Option(Option.OptionEnum.FEATURES));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenOptionIsUnknown(){
        assertThrows(IllegalStateException.class, () -> {
           optionFactory.createOption("--unknown");
        });
    }

    @Test
    void shouldReturnGoodOption(){
        assertAll(
                () -> {
                    assertEquals(Option.OptionEnum.INFO, optionFactory.createOption("--info").getOption());
                },
                () -> {
                    assertEquals(Option.OptionEnum.HELP, optionFactory.createOption("--help").getOption());
                },
                () -> {
                    assertEquals(Option.OptionEnum.FEATURES, optionFactory.createOption("--features").getOption());
                },
                () -> {
                    assertEquals(Option.OptionEnum.TARGET, optionFactory.createOption("--target").getOption());
                }
        );
    }

}