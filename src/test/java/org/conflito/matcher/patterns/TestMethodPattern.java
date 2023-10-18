package org.conflito.matcher.patterns;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.FieldAccessType;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.Type;
import org.conflito.matcher.entities.Visibility;
import org.junit.jupiter.api.Test;
import spoon.reflect.factory.TypeFactory;

public class TestMethodPattern {

  private final TypeFactory FACTORY = new TypeFactory();
  private final Type INT_TYPE = new Type(FACTORY.get(int.class).getReference());

  @Test
  public void matchingSimpleMethod() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    FreeVariable freeVar = new FreeVariable(0);
    MethodPattern pattern = new MethodPattern(freeVar, Visibility.PUBLIC);
    pattern.setVariableValue(0, instance.getQualifiedName());
    assertTrue(pattern.matches(instance), "Simple method doesn't match?");
  }

  @Test
  public void matchingSimpleMethodAnyVisibility() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    FreeVariable freeVar = new FreeVariable(0);
    MethodPattern pattern = new MethodPattern(freeVar, null);
    pattern.setVariableValue(0, instance.getQualifiedName());
    assertTrue(pattern.matches(instance), "Simple method with any visibility doesn't match?");
  }

  @Test
  public void matchingSimpleMethodDifferentVisibility() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    FreeVariable freeVar = new FreeVariable(0);
    MethodPattern pattern = new MethodPattern(freeVar, Visibility.PRIVATE);
    pattern.setVariableValue(0, instance.getQualifiedName());
    assertFalse(pattern.matches(instance), "Simple method different visibility matches?");
  }

  @Test
  public void matchingSimpleMethodDifferentName() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    FreeVariable freeVar = new FreeVariable(0);
    MethodPattern pattern = new MethodPattern(freeVar, Visibility.PUBLIC);
    pattern.setVariableValue(0, "n()");
    assertFalse(pattern.matches(instance), "Simple method with different name and matches?");
  }

  @Test
  public void matchingSimpleMethodDifferentNameAndVisibility() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    FreeVariable freeVar = new FreeVariable(0);
    MethodPattern pattern = new MethodPattern(freeVar, Visibility.PRIVATE);
    pattern.setVariableValue(0, "n()");
    assertFalse(pattern.matches(instance), "Simple method with different name and "
        + "visibility matches?");
  }

  @Test
  public void matchingMethodWithOneInvocation() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    pattern.addDependency(freeVar1);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "m()");

    assertTrue(pattern.matches(instance), "Method with one invocation doesn't match?");
  }

  @Test
  public void matchingMethodWithOneFieldAccess() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar1, FieldAccessType.READ);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "x");

    assertTrue(pattern.matches(instance), "Method with one field access doesn't match?");
  }

  @Test
  public void matchingMethodWithOneAnyFieldAccess() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar1, null);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "x");

    assertTrue(pattern.matches(instance), "Method with one any field access doesn't match?");
  }

  @Test
  public void matchingMethodWithOneInvocationNotFullyFilled() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    pattern.addDependency(freeVar1);

    pattern.setVariableValue(1, "m()");

    assertFalse(pattern.matches(instance), "Method with one invocation "
        + "not filled matches?");
  }

  @Test
  public void matchingMethodWithOneFieldAccessNotFullyFilled() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar1, FieldAccessType.READ);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());

    assertFalse(pattern.matches(instance), "Method with one field access "
        + "not filled matches?");
  }

  @Test
  public void matchingMethodWithTwoInvocationPatternRequiresOne() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    MethodInstance instance2 = new MethodInstance("n", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);
    instance.addDirectDependency(instance2);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    pattern.addDependency(freeVar1);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "m()");

    assertTrue(pattern.matches(instance), "Method with two invocations "
        + "and pattern requiring one doesn't match first invocation?");

    pattern.setVariableValue(1, "n()");

    assertTrue(pattern.matches(instance), "Method with two invocations "
        + "and pattern requiring one doesn't match second invocation?");
  }

  @Test
  public void matchingMethodWithTwoFieldAccessesPatternRequiresOne() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    FieldAccessInstance fieldAccessInstance2 =
        new FieldAccessInstance("y", FieldAccessType.WRITE);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addFieldAccess(fieldAccessInstance);
    instance.addFieldAccess(fieldAccessInstance2);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar1, null);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "x");

    assertTrue(pattern.matches(instance), "Method with two accesses "
        + "and pattern requiring one doesn't match first access?");

    pattern.setVariableValue(1, "y");

    assertTrue(pattern.matches(instance), "Method with two accesses "
        + "and pattern requiring one doesn't match second access?");
  }

  @Test
  public void matchingMethodWithOneInvocationButNoMatch() {
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    pattern.addDependency(freeVar1);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "n()");

    assertFalse(pattern.matches(instance), "Method with one invocation and "
        + "pattern with a different value matches?");
  }

  @Test
  public void matchingMethodWithOneFieldAccessButNoMatch() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar1, null);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "y");

    assertFalse(pattern.matches(instance), "Method with one field access and "
        + "pattern with a different value matches?");
  }

  @Test
  public void matchingMethodWithInvocationAndFieldAccess() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);
    FreeVariable freeVar2 = new FreeVariable(2);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar2, null);
    pattern.addDependency(freeVar1);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "m()");
    pattern.setVariableValue(2, "x");

    assertTrue(pattern.matches(instance), "Method with one invocation and "
        + "access doesn't match?");

  }

  @Test
  public void matchingMethodWithInvocationAndWrongFieldAccess() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);
    FreeVariable freeVar2 = new FreeVariable(2);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar2, null);
    pattern.addDependency(freeVar1);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "m()");
    pattern.setVariableValue(2, "y");

    assertFalse(pattern.matches(instance), "Method with one invocation and "
        + "wrong access matches?");

  }

  @Test
  public void matchingMethodWithWrongInvocationAndFieldAccess() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);
    FreeVariable freeVar2 = new FreeVariable(2);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar2, null);
    pattern.addDependency(freeVar1);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "n()");
    pattern.setVariableValue(2, "x");

    assertFalse(pattern.matches(instance), "Method with wrong one invocation and "
        + "access matches?");
  }

  @Test
  public void matchingMethodWithInvocationAndFieldAccessBothWrong() {
    FieldAccessInstance fieldAccessInstance =
        new FieldAccessInstance("x", FieldAccessType.READ);
    MethodInstance instance = new MethodInstance("m", Visibility.PUBLIC, INT_TYPE);
    instance.addDirectDependency(instance);
    instance.addFieldAccess(fieldAccessInstance);

    FreeVariable freeVar0 = new FreeVariable(0);
    FreeVariable freeVar1 = new FreeVariable(1);
    FreeVariable freeVar2 = new FreeVariable(2);

    MethodPattern pattern = new MethodPattern(freeVar0, Visibility.PUBLIC);
    FieldAccessPattern fieldAccessPattern =
        new FieldAccessPattern(freeVar2, null);
    pattern.addDependency(freeVar1);
    pattern.addFieldAccessPattern(fieldAccessPattern);

    pattern.setVariableValue(0, instance.getQualifiedName());
    pattern.setVariableValue(1, "n()");
    pattern.setVariableValue(2, "y");

    assertFalse(pattern.matches(instance), "Method with wrong one invocation and "
        + "wrong access matches?");
  }

}
