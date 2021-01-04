package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;

import gumtree.spoon.AstComparator;
import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import matcher.utils.SpoonLauncherUtils;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;


public class BaseInstanceHandler {

	public BaseInstance getBaseInstance(File base, ConflictPattern cp) throws ApplicationException {
		SpoonResource resource = null;
		Launcher launcher = new Launcher();
		try {
			resource = SpoonResourceHelper.createFile(base);
			launcher.addInputResource(resource);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid specified file", e);
		}
		CtType<?> changedType = new AstComparator().getCtType(resource);
		if(changedType.isClass()) {
			CtClass<?> changedClass = (CtClass<?>) new AstComparator().getCtType(resource);
			SpoonLauncherUtils.loadClassTree(changedClass, launcher);
			SpoonLauncherUtils.loadInvokedClasses(changedClass, launcher);

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
