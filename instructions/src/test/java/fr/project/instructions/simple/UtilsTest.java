package fr.project.instructions.simple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class UtilsTest {

    @Test
    void testCreateListOfConstantForConcatenationSimpleTest(){
        var format = "\u00011s\u00015";
        var listForFormat = new ArrayList<String>();
        listForFormat.add("arg");listForFormat.add("1s");listForFormat.add("arg");listForFormat.add("5");
        assertEquals(listForFormat, Utils.createListOfConstantForConcatenation(format));
    }

    @Test
    void testCreateListOfConstantForConcatenationSimpleTest2(){
        var format = "\u0001 1 s \u00015";
        var listForFormat = new ArrayList<String>();
        listForFormat.add("arg");listForFormat.add(" 1 s ");listForFormat.add("arg");listForFormat.add("5");
        assertEquals(listForFormat, Utils.createListOfConstantForConcatenation(format));
    }

    @Test
    void testNumberOfArgOccurrence(){
        var format1 = "\u00011s\u00015";
        var format2 = "\u0001123455";
        var format3 = "\u0001\u0001\u0001";
        assertAll(
                () -> {
                    assertEquals(2, Utils.numberOfOccurrence(Utils.createListOfConstantForConcatenation(format1), "arg"));
                },
                () -> {
                    assertEquals(1, Utils.numberOfOccurrence(Utils.createListOfConstantForConcatenation(format2), "arg"));
                },
                () -> {
                    assertEquals(3, Utils.numberOfOccurrence(Utils.createListOfConstantForConcatenation(format3), "arg"));
                }
        );
    }

    @Test
    void testTakeOwnerFunctionCorrectly(){
        var s = "applyAsInt:(I)Ljava/util/function/IntUnaryOperator;";
        assertEquals("java/util/function/IntUnaryOperator", Utils.takeOwnerFunction(s));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenNotLToSeparateObject(){
        var s = "applyAsInt:(I)java/util/function/IntUnaryOperator;";
        assertAll(
                () -> {
                    assertThrows(IllegalStateException.class, () -> Utils.takeOwnerFunction(s));
                },
                () -> {
                    assertThrows(IllegalStateException.class, () -> Utils.takeCapture(s));
                }
        );
    }

    @Test
    void testTakeCaptureCorrectly(){
        var s = "(I)Ljava/util/function/IntUnaryOperator;";
        assertEquals("I", Utils.takeCapture(s));
    }

}
