package matcher.handlers;

import java.io.File;

import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import matcher.processors.InterfaceProcessor;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {
	
	private SpoonHandler spoonHandler;
	
	public BaseInstanceHandler(SpoonHandler spoonHandler) {
		this.spoonHandler = spoonHandler;
	}

	public BaseInstance getBaseInstance(File base, ConflictPattern cp) throws ApplicationException {
		SpoonResource resource = null;
		
		resource = spoonHandler.getSpoonResource(base);
		
		CtType<?> changedType = spoonHandler.getCtType(resource);
		if(changedType.isClass()) {
			Launcher launcher = spoonHandler.loadLauncher(resource);

			BaseInstance result = new BaseInstance();
			for(CtType<?> t: launcher.buildModel().getAllTypes()) {
				if(t.isClass()) {
					ClassProcessor processor = new ClassProcessor(cp);
					processor.process((CtClass<?>)t);
					result.addClassInstance(processor.getClassInstance());
				}
				if(t.isInterface() && cp.hasInterfaces()) {
					InterfaceProcessor processor = new InterfaceProcessor();
					processor.process(t.getReference());
					result.addInterfaceInstance(processor.getInterfaceInstance());
				}
			}

			return result;
		}
		return null;
	}
}
