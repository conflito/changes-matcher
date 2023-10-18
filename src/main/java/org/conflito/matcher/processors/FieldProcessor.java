package org.conflito.matcher.processors;

import org.conflito.matcher.entities.FieldInstance;
import org.conflito.matcher.entities.Type;
import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.handlers.InstancesCache;
import spoon.reflect.declaration.CtField;

public class FieldProcessor implements Processor<FieldInstance, CtField<?>> {

  @Override
  public FieldInstance process(CtField<?> element) {
    if (element == null) {
      return null;
    }
    if (InstancesCache.getInstance().hasField(element)) {
      return new FieldInstance(InstancesCache.getInstance().getField(element));
    }
    Visibility visibility = Visibility.PACKAGE;
    if (element.getVisibility() != null) {
      visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
    }
    String fieldName = element.getSimpleName();
    Type type = new Type(element.getType());

    FieldInstance fieldInstance = new FieldInstance(fieldName, visibility, type);

    InstancesCache.getInstance().putField(element, fieldInstance);

    return fieldInstance;
  }

}
