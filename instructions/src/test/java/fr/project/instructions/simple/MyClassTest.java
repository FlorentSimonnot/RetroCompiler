package fr.project.instructions.simple;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MyClassTest {

    @Test
    void shouldThrowNullPointerExceptionWhenNameOrOwnerIsNull(){
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> {
                    new MyClass(Opcodes.ACC_PUBLIC, null, "java/lang/Object", new String[]{});
                }),
                () -> assertThrows(NullPointerException.class, () -> {
                    new MyClass(Opcodes.ACC_PUBLIC, "MyClass", null, new String[]{});
                })
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPrivacyIsNegative(){
        assertThrows(IllegalArgumentException.class, () -> new MyClass(-1, "a", "b", new String[]{}));
    }

    @Test
    void shouldSayIfClassIsARecordClassCorrectly(){
        assertAll(
                () -> {
                    assertTrue(new MyClass(Opcodes.ACC_PUBLIC, "MyRecordClass", "java/lang/Record", new String[]{}).isRecordClass());
                },
                () -> {
                    assertFalse(new MyClass(Opcodes.ACC_PUBLIC, "MyRecordClass", "java/lang/Object", new String[]{}).isRecordClass());
                },
                () -> {
                    assertFalse(new MyClass(Opcodes.ACC_PUBLIC, "MyRecordClass", "MySuperClass", new String[]{}).isRecordClass());
                }
        );
    }

    @Test
    void shouldGettersWorkCorrectly(){
        var interfaces = new String[]{"Instruction"};
        var myClass = new MyClass(Opcodes.ACC_PUBLIC, "MyClass", "java/lang/AutoCloseable", interfaces);
        assertAll(
                () -> {
                    assertEquals("MyClass", myClass.getClassName());
                },
                () -> {
                    assertEquals(Opcodes.ACC_PUBLIC, myClass.getPrivacy());
                },
                () -> {
                    assertEquals("Instruction", myClass.getInterfaces()[0]);
                },
                () -> {
                    assertEquals(1, myClass.getInterfaces().length);
                },
                () -> {
                    assertEquals(interfaces, myClass.getInterfaces());
                }
        );
    }

    @Test
    void shouldAddFieldAndMethodsCorrectly(){
        var myClass = new MyClass(Opcodes.ACC_PUBLIC, "MyClass", "java/lang/AutoCloseable", new String[]{});
        var f = new Field(Opcodes.ACC_PRIVATE, "myField", "Z", "",  true);
        var m = new Method(Opcodes.ACC_PUBLIC, "myMethod", "(I)Ljava/lang/String;", "", false, new String[]{});
        myClass.addField(f);
        myClass.addMethod(m);
        var listField = new ArrayList<Field>();
        listField.add(f);
        var listMethod = new ArrayList<Method>();
        listMethod.add(m);
        assertAll(
                () -> {
                    assertEquals(listField, myClass.getFields());
                },
                () -> {
                    assertEquals(listMethod, myClass.getMethods());
                },
                () -> {
                    assertEquals(f, myClass.getFields().get(0));
                },
                () -> {
                    assertEquals(m, myClass.getMethods().get(0));
                }
        );
    }

    @Test
    void shouldReturnTwoConstructorsOnFiveMethods(){
        var myClass = new MyClass(Opcodes.ACC_PUBLIC, "MyClass", "java/lang/Object", new String[]{});
        IntStream.range(0, 5).forEach(i  -> {
            if(i < 3){
                myClass.addMethod(new Method(Opcodes.ACC_PUBLIC, "<init>", "(II)", "(II)", false, new String[]{}));
            }else{
                myClass.addMethod(new Method(Opcodes.ACC_PUBLIC, "myMethod"+i, "(V)", "(V)", false, new String[]{}));
            }
        });
        assertAll(
                () -> assertEquals(3, myClass.getConstructors().size()),
                () -> assertEquals(2, myClass.getMethods().size()),
                () -> assertEquals(5, myClass.getAllMethods().size())
        );

    }

    @Test
    void shouldSettersWorkCorrectly(){
        var myClass = new MyClass(Opcodes.ACC_PUBLIC, "MyClass", "java/lang/Object", new String[]{});
        myClass.setLineNumber(3);
        myClass.setSourceName("MyClass.class");
        assertAll(
                () -> assertEquals(3, myClass.getLineNumber()),
                () -> assertEquals("MyClass.class", myClass.getSourceName())
        );
    }
}
