package fr.project.instructions.simple;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.jupiter.api.Assertions.*;

class FieldInstructionTest {

    @Test
    void shouldThrowIllegalArgumentExceptionWhenOpcodeIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> new FieldInstruction("field", "MyClass", -1, "I"));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameOrOwnerOrDescriptorIsNull(){
        assertAll(
                () -> {
                    assertThrows(NullPointerException.class, () -> new FieldInstruction(null, "M", Opcodes.PUTFIELD, "I"));
                },
                () -> {
                    assertThrows(NullPointerException.class, () -> new FieldInstruction("field", null, Opcodes.PUTFIELD, "I"));
                },
                () -> {
                    assertThrows(NullPointerException.class, () -> new FieldInstruction("field", "M", Opcodes.PUTFIELD, null));
                }
        );
    }

    @Test
    void shouldGetTypeCorrectly(){
        assertAll(
                () -> {
                    assertEquals("(I)", new FieldInstruction("f", "c", Opcodes.ILOAD, "I").getType());
                },
                () -> {
                    assertEquals("(J)", new FieldInstruction("f", "c", Opcodes.LLOAD, "L").getType());
                },
                () -> {
                    assertEquals("(B)", new FieldInstruction("f", "c", Opcodes.PUTFIELD, "B").getType());
                },
                () -> {
                    assertEquals("(Ljava/lang/String)", new FieldInstruction("f", "c", Opcodes.GETFIELD, "Ljava/lang/String").getType());
                },
                () -> {
                    assertEquals("(Ljava/lang/Object;)", new FieldInstruction("f", "c", Opcodes.ALOAD, "Ljava/lang/String").getType());
                }
        );
    }


}
