package matcher.processors;

import matcher.entities.FieldInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;

public class FieldProcessor extends AbstractProcessor<CtField<?>>{

	private FieldInstance fieldInstance;
		
	public FieldInstance getFieldInstance() {
		return fieldInstance;
	}

	@Override
	public void process(CtField<?> element) {
		Visibility visibility = Visibility.PACKAGE;
		if(element.getVisibility() != null)
			visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
		String fieldName = element.getSimpleName();
		Type type = new Type(element.getType());
		fieldInstance = new FieldInstance(fieldName, visibility, type);
	}

}
