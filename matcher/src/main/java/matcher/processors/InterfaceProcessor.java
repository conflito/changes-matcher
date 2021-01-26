package matcher.processors;

import matcher.entities.InterfaceInstance;
import spoon.reflect.reference.CtTypeReference;

public class InterfaceProcessor extends Processor<InterfaceInstance, CtTypeReference<?>>{

	@Override
	public InterfaceInstance process(CtTypeReference<?> element) {
		return new InterfaceInstance(element.getQualifiedName());
	}

}
