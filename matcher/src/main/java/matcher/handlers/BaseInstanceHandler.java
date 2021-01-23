package matcher.handlers;


import matcher.entities.BaseInstance;

import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import matcher.processors.InterfaceProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {
		
	public BaseInstanceHandler() {

	}
	
	public BaseInstance getBaseInstance(CtType<?> type, ConflictPattern cp) {
		BaseInstance result = new BaseInstance();
		if(type.isClass()) {
			ClassProcessor processor = new ClassProcessor(cp);
			processor.process((CtClass<?>)type);
			result.addClassInstance(processor.getClassInstance());
		}
		if(type.isInterface() && cp.hasInterfaces()) {
			InterfaceProcessor processor = new InterfaceProcessor();
			processor.process(type.getReference());
			result.addInterfaceInstance(processor.getInterfaceInstance());
		}
		return result;
	}
}
