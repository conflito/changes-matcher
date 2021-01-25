package matcher.handlers;


import java.util.HashMap;
import java.util.Map;

import matcher.entities.BaseInstance;
import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.patterns.ConflictPattern;
import matcher.entities.MethodInstance;
import matcher.processors.ClassProcessor;
import matcher.processors.InterfaceProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {
	
	private Map<String, MethodInstance> methodsByQualifiedName;
	private Map<String, ConstructorInstance> constructorsByQualifiedName;
		
	public BaseInstanceHandler() {
		methodsByQualifiedName = new HashMap<>();
		constructorsByQualifiedName = new HashMap<>();
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
	
	public BaseInstance getBaseInstance(Iterable<CtType<?>> types, ConflictPattern cp) {
		BaseInstance result = new BaseInstance();
		for(CtType<?> type: types) {
			if(type.isClass()) {
				ClassProcessor processor = new ClassProcessor(cp);
				processor.process((CtClass<?>)type);
				ClassInstance classInstance = processor.getClassInstance();
				result.addClassInstance(classInstance);
				for(MethodInstance m: classInstance.getMethods()) {
					String key = classInstance.getQualifiedName() + "." + m.getQualifiedName();
					methodsByQualifiedName.put(key, m);
				}
				for(ConstructorInstance c: classInstance.getConstructors()) {
					String key = c.getQualifiedName();
					constructorsByQualifiedName.put(key, c);
				}
			}
			if(type.isInterface() && cp.hasInterfaces()) {
				InterfaceProcessor processor = new InterfaceProcessor();
				processor.process(type.getReference());
				result.addInterfaceInstance(processor.getInterfaceInstance());
			}
		}
		for(MethodInstance m: methodsByQualifiedName.values()) {
			for(String s: m.getDirectDependenciesNames()) {
				if(methodsByQualifiedName.containsKey(s)) {
					m.addDirectDependency(methodsByQualifiedName.get(s));
				}
			}
		}
		for(ConstructorInstance c: constructorsByQualifiedName.values()) {
			for(String s: c.getDirectDependenciesNames()) {
				if(methodsByQualifiedName.containsKey(s))
					c.addDirectDependency(methodsByQualifiedName.get(s));
			}
		}
		
		return result;
	}
}
