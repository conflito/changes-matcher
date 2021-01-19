package matcher.processors;

import matcher.entities.InterfaceInstance;
import spoon.processing.AbstractProcessor;
import spoon.reflect.reference.CtTypeReference;

public class InterfaceProcessor extends AbstractProcessor<CtTypeReference<?>>{

	private InterfaceInstance interfaceInstance;
	
	public InterfaceInstance getInterfaceInstance() {
		return interfaceInstance;
	}

	@Override
	public void process(CtTypeReference<?> element) {
		interfaceInstance = new InterfaceInstance(element.getSimpleName());
	}

}
