package matcher.handlers;

import matcher.entities.BaseInstance;
import matcher.entities.ClassInstance;
import matcher.entities.InterfaceInstance;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import matcher.processors.InterfaceProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {
		
	public BaseInstanceHandler() {

	}
	
	public BaseInstance getBaseInstance(Iterable<CtType<?>> types, ConflictPattern cp) {
		BaseInstance result = new BaseInstance();
		for(CtType<?> type: types) {
			if(type.isClass()) {
				processBase(result, type, cp);
			}
			if(type.isInterface() && cp.hasInterfaces()) {
				processInterface(result, type);
			}
		}
		
		return result;
	}
	
	private void processBase(BaseInstance result, CtType<?> type, ConflictPattern cp) {
		CtClass<?> ctClass = (CtClass<?>)type;
		ClassProcessor processor = new ClassProcessor(cp);
		ClassInstance classInstance = processor.process(ctClass);
		if(classInstance != null) {
			result.addClassInstance(classInstance);
			if(!ctClass.getNestedTypes().isEmpty()) {
				result.merge(getBaseInstance(ctClass.getNestedTypes(), cp));
			}
		}
	}
	
	private void processInterface(BaseInstance result, CtType<?> type) {
		InterfaceProcessor processor = new InterfaceProcessor();
		InterfaceInstance instance = processor.process(type.getReference());
		if(instance != null)
			result.addInterfaceInstance(instance);
	}
}
