package fr.project.optionsCommand;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionUtilsTest {

    @Test
    void shouldThrowIllegalStateExceptionWhenWePassedArgumentOnOptionWhichDoesntTakeArgument(){
        assertAll(
                () -> {
                    assertThrows(IllegalStateException.class, () -> {
                        OptionUtils.checkArgument(Option.OptionEnum.HELP, "7");
                    });
                },
                () -> {
                    assertThrows(IllegalStateException.class, () -> {
                        OptionUtils.checkArgument(Option.OptionEnum.INFO, "No");
                    });
                }
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenVersionForTargetOptionIsInCorrect(){
        assertAll(
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> OptionUtils.checkArgument(Option.OptionEnum.TARGET, "3"));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> OptionUtils.checkArgument(Option.OptionEnum.TARGET, "1"));
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> OptionUtils.checkArgument(Option.OptionEnum.TARGET, "42"));
                }
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenListOfFeaturesIsEmpty(){
        assertAll(
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> {
                       OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[]");
                    });
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> {
                        OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[*]");
                    });
                },
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> {
                        OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[,]");
                    });
                }
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenListOfFeaturesContainsUnknownFeature(){
        assertAll(
                () -> {
                    assertThrows(IllegalArgumentException.class, () -> {
                        OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[try-with-resources, joke]");
                    });
                }
        );
    }

    @Test
    void testCheckArgumentForValidArguments(){
        assertAll(
                () -> {
                    assertEquals("7", OptionUtils.checkArgument(Option.OptionEnum.TARGET, "7"));
                },
                () -> {
                    assertEquals("[try-with-resources,record]", OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[try-with-resources,record]"));
                },
                () -> {
                    assertEquals("[try-with-resources,record]", OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[try-with-resources, record]"));
                },
                () -> {
                    assertEquals("[try-with-resources,record,lambda]", OptionUtils.checkArgument(Option.OptionEnum.FEATURES, "[try-with-resources,    record     , lambda]"));
                }
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNullIsPassedForCheckArgumentMethod(){
        assertAll(
                () -> {
                    assertThrows(NullPointerException.class, () -> OptionUtils.checkArgument(null, "joke"));
                },
                () -> {
                    assertThrows(NullPointerException.class, () -> OptionUtils.checkArgument(Option.OptionEnum.FEATURES, null));
                }
        );
    }
}