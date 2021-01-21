package matcher.processors;

import matcher.entities.InterfaceImplementationInstance;
import spoon.processing.AbstractProcessor;
import spoon.reflect.reference.CtTypeReference;

public class InterfaceProcessor extends AbstractProcessor<CtTypeReference<?>>{

	private InterfaceImplementationInstance interfaceInstance;
	
	public InterfaceImplementationInstance getInterfaceInstance() {
		return interfaceInstance;
	}

	@Override
	public void process(CtTypeReference<?> element) {
		interfaceInstance = new InterfaceImplementationInstance(element.getSimpleName());
	}

}
