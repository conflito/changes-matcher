package org.conflito.matcher.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import spoon.reflect.factory.TypeFactory;

public class TestType {

  private final TypeFactory factory = new TypeFactory();

  private final Type[] types = {
      new Type(factory.BOOLEAN_PRIMITIVE),
      new Type(factory.INTEGER_PRIMITIVE),
      new Type(factory.BYTE_PRIMITIVE),
      new Type(factory.SHORT_PRIMITIVE),
      new Type(factory.CHARACTER_PRIMITIVE),
      new Type(factory.LONG_PRIMITIVE),
      new Type(factory.FLOAT_PRIMITIVE),
      new Type(factory.DOUBLE_PRIMITIVE)
  };

  @Test
  public void isSubtypeWithTrueSubtypeTest() {
    Type subType = new Type(factory.INTEGER);
    Type topType = new Type(factory.get(Number.class).getReference());
    assertTrue(subType.isSubtypeOf(topType), "Integer not subtype of Number?");
  }

  @Test
  public void isSubtypeWithFalseSubtypeTest() {
    Type subType = new Type(factory.INTEGER);
    Type topType = new Type(factory.STRING);
    assertFalse(subType.isSubtypeOf(topType), "Integer subtype of String?");
  }

  @Test
  public void primitiveToWrapperWithPrimitiveTest() {
    Type primitive = new Type(factory.BOOLEAN_PRIMITIVE);
    Type wrapper = Type.primitiveToWrapper(primitive);
    Type expected = new Type(factory.BOOLEAN);
    assertEquals(expected, wrapper, "Not the Boolean type?");
  }

  @Test
  public void primitiToWrapperWithNonPrimitiveTest() {
    Type type = new Type(factory.get(Number.class).getReference());
    Type wrapper = Type.primitiveToWrapper(type);
    Type expected = null;
    assertEquals(expected, wrapper, "Number converted to wrapper class?");
  }

  @Test
  public void primitiveTypesDescriptorTest() {
    String[] expectedValues = {"Z", "I", "B", "S", "C", "J", "F", "D"};
    for (int i = 0; i < expectedValues.length; i++) {
      Type primitive = types[i];
      String expected = expectedValues[i];
      String result = primitive.getDescriptor();
      assertEquals(expected, result, "Wrong primitive descritor?");
    }
  }

  @Test
  public void numberTypeDescriptorTest() {
    Type type = new Type(factory.get(Number.class).getReference());
    String expected = "Ljava/lang/Number;";
    String result = type.getDescriptor();
    assertEquals(expected, result, "Descriptor of type Number is not"
        + "Ljava/lang/Number;?");
  }

  @Test
  public void objectTypeDescriptorTest() {
    Type type = new Type(factory.get(Object.class).getReference());
    String expected = "Ljava/lang/Object;";
    String result = type.getDescriptor();
    assertEquals(expected, result, "Descriptor of type Object is not"
        + "Ljava/lang/Object;?");
  }

  @Test
  public void typeTypeDescriptorTest() {
    Type type = new Type(factory.get(Type.class).getReference());
    String expected = "Lorg/conflito/matcher/entities/Type;";
    String result = type.getDescriptor();
    assertEquals(expected, result, "Descriptor of type Type is not"
        + "Lmatcher/entities/Type;?");
  }

  @Test
  public void doubleArrayDescriptorTest() {
    Type type = new Type(factory.createArrayReference(factory.DOUBLE_PRIMITIVE));
    String expected = "[D";
    String result = type.getDescriptor();
    assertEquals(expected, result, "Descriptor of double[] is not"
        + "[D?");
  }

  @Test
  public void doubleTwoDimensionalArrayDescriptorTest() {
    Type type = new Type(factory.createArrayReference(factory.DOUBLE_PRIMITIVE, 2));
    String expected = "[[D";
    String result = type.getDescriptor();
    assertEquals(expected, result, "Descriptor of double[][] is not"
        + "[[D?");
  }

  @Test
  public void numberArrayDescriptorTest() {
    Type type = new Type(factory.createArrayReference(factory.get(Number.class).getReference()));
    String expected = "[Ljava/lang/Number;";
    String result = type.getDescriptor();
    assertEquals(expected, result, "Descriptor of Number[] is not"
        + "[Ljava/lang/Number;?");
  }
}
