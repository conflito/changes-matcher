package org.conflito.matcher.processors;

import org.conflito.matcher.entities.InterfaceInstance;
import spoon.reflect.reference.CtTypeReference;

public class InterfaceProcessor implements Processor<InterfaceInstance, CtTypeReference<?>>{

	@Override
	public InterfaceInstance process(CtTypeReference<?> element) {
		if(element == null)
			return null;
		return new InterfaceInstance(element.getQualifiedName());
	}

}
