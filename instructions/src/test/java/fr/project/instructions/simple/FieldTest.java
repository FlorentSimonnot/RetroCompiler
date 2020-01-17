package fr.project.instructions.simple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void shouldThrowIllegalArgumentExceptionWhenAccessIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> new Field(-1, "field", "I", "I", 3));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameOrDescriptorIsNull(){
        assertAll(
                () -> {
                    assertThrows(NullPointerException.class, () -> new Field(3, null, "I", "I", 3));
                },
                () -> {
                    assertThrows(NullPointerException.class, () -> new Field(3, "field", null, "I", 3));
                }
        );
    }

    @Test
    void shouldGetGettersCorrectly(){
        final var field = new Field(3, "field", "I", "I", 3);
        assertAll(
                () -> {
                    assertEquals(3, field.getAccess());
                },
                () -> {
                    assertEquals("field", field.getName());
                },
                () -> {
                    assertEquals("I", field.getDescriptor());
                },
                () -> {
                    assertEquals("I", field.getSignature());
                },
                () -> {
                    assertEquals(3, field.getValue());
                }
        );
    }

}
