package matcher.processors;

import matcher.entities.InterfaceInstance;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtInterface;

public class InterfaceProcessor extends AbstractProcessor<CtInterface<?>>{

	private InterfaceInstance interfaceInstance;
	
	public InterfaceInstance getInterfaceInstance() {
		return interfaceInstance;
	}

	@Override
	public void process(CtInterface<?> element) {
		interfaceInstance = new InterfaceInstance(element.getSimpleName());
	}

}
