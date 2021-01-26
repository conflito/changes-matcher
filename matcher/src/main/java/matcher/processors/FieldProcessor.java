package matcher.processors;

import matcher.entities.FieldInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import spoon.reflect.declaration.CtField;

public class FieldProcessor extends Processor<FieldInstance, CtField<?>>{
		
	@Override
	public FieldInstance process(CtField<?> element) {
		Visibility visibility = Visibility.PACKAGE;
		if(element.getVisibility() != null)
			visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
		String fieldName = element.getSimpleName();
		Type type = new Type(element.getType());
		return new FieldInstance(fieldName, visibility, type);
	}

}
