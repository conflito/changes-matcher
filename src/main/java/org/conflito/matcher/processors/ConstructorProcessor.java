package org.conflito.matcher.processors;

import java.util.List;
import java.util.stream.Collectors;
import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.Type;
import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.handlers.InstancesCache;
import org.conflito.matcher.handlers.SpoonHandler;
import org.conflito.matcher.patterns.ConflictPattern;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.visitor.filter.TypeFilter;

public class ConstructorProcessor implements Processor<ConstructorInstance, CtConstructor<?>> {

  private final ConflictPattern conflictPattern;

  public ConstructorProcessor(ConflictPattern conflicPattern) {
    this.conflictPattern = conflicPattern;
  }

  @Override
  public ConstructorInstance process(CtConstructor<?> element) {
    if (element == null) {
      return null;
    }
    if (InstancesCache.getInstance().hasConstructor(element)) {
      ConstructorInstance result = InstancesCache.getInstance().getConstructor(element);
      if (conflictPattern.hasInvocations() && !result.hasDependencies()) {
        processMethodInvocations(element, result);
        InstancesCache.getInstance().putConstructor(element, result);
      }
      return new ConstructorInstance(result);
    }
    Visibility visibility = Visibility.PACKAGE;
    if (element.getVisibility() != null) {
      visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
    }
    List<Type> parameters = element.getParameters().stream()
        .map(p -> new Type(p.getType()))
        .collect(Collectors.toList());
    ConstructorInstance constructorInstance = new ConstructorInstance(visibility, parameters);
    if (conflictPattern.hasInvocations()) {
      processMethodInvocations(element, constructorInstance);
    }

    InstancesCache.getInstance().putConstructor(element, constructorInstance);

    return constructorInstance;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void processMethodInvocations(CtConstructor<?> element
      , ConstructorInstance constructorInstance) {
    List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
    for (CtInvocation<?> invocation : invocations) {
      if (SpoonHandler.validInvocation(invocation)) {
        try {
          if (SpoonHandler.invocationFromTheSystem(invocation)) {
            MethodInstance invoked =
                new MethodProcessor(conflictPattern).process(
                    SpoonHandler.getMethodFromInvocation(invocation));
            constructorInstance.addDirectDependency(invoked);
          }
        } catch (Exception e) {
        }

      }
    }
  }
}
