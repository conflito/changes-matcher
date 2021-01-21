package matcher.handlers;

import java.io.File;

import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import matcher.processors.InterfaceProcessor;
import matcher.utils.SpoonUtils;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {

	public BaseInstance getBaseInstance(File base, ConflictPattern cp) throws ApplicationException {
		SpoonResource resource = null;
		
		resource = SpoonUtils.getSpoonResource(base);
		
		CtType<?> changedType = SpoonUtils.getCtType(resource);
		if(changedType.isClass()) {
			Launcher launcher = SpoonUtils.loadLauncher(resource);

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
