package matcher.handlers;

import java.io.File;

import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import matcher.utils.SpoonUtils;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {

	public BaseInstance getBaseInstance(File base, ConflictPattern cp) throws ApplicationException {
		SpoonResource resource = null;
		Launcher launcher = new Launcher();
		resource = SpoonUtils.getSpoonResource(base);
		launcher.addInputResource(resource);
		
		CtType<?> changedType = SpoonUtils.getCtType(resource);
		if(changedType.isClass()) {
			CtClass<?> changedClass = SpoonUtils.getCtClass(resource);
			SpoonUtils.loadClassTree(changedClass, launcher);
			SpoonUtils.loadInvokedClasses(changedClass, launcher);

			BaseInstance result = new BaseInstance();
			for(CtType<?> t: launcher.buildModel().getAllTypes()) {
				ClassProcessor processor = new ClassProcessor(cp);
				processor.process((CtClass<?>)t);
				result.addClassInstance(processor.getClassInstance());
			}

			return result;
		}
		return null;
	}
}
