package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gumtree.spoon.AstComparator;
import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

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
		CtClass<?> changedClass = (CtClass<?>) new AstComparator().getCtType(resource);
		loadClassTree(changedClass, launcher);
		loadInvokedClasses(changedClass, launcher);
		
		BaseInstance result = new BaseInstance();
		for(CtType<?> t: launcher.buildModel().getAllTypes()) {
			ClassProcessor processor = new ClassProcessor(cp);
			processor.process((CtClass<?>)t);
			result.addClassInstance(processor.getClassInstance());
		}
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadInvokedClasses(CtClass<?> changedClass, Launcher launcher) 
			throws ApplicationException {
		List<CtInvocation<?>> invocations = 
				changedClass.getElements(new TypeFilter(CtInvocation.class));
		invocations = invocations.stream()
								 .filter(i -> !i.toString().equals("super()") &&
									!getInvocationClassQualifiedName(i)
										.equals(changedClass.getQualifiedName()))
								 .collect(Collectors.toList());
		for(CtInvocation<?> invocation: invocations) {
			String simpleName = invocation.getExecutable().getDeclaringType().getSimpleName();
			Optional<File> srcFile = FileSystemHandler.getInstance().getSrcFile(simpleName + ".java");
			if(srcFile.isPresent()) {
				launcher.addInputResource(srcFile.get().getAbsolutePath());
			}
		}
	}

	private void loadClassTree(CtClass<?> changedClass, Launcher launcher) throws ApplicationException {
		CtTypeReference<?> superClass = changedClass.getSuperclass();
		if(superClass != null) {
			Optional<File> superFile = 
					FileSystemHandler.getInstance().getSrcFile(superClass.getSimpleName() + ".java");
			if(superFile.isPresent()) {
				File sf = superFile.get();
				launcher.addInputResource(sf.getAbsolutePath());
				try {
					SpoonResource resource = SpoonResourceHelper.createFile(sf);
					CtClass<?> sc = (CtClass<?>) new AstComparator().getCtType(resource);
					loadClassTree(sc, launcher);
				} catch (FileNotFoundException e) {
					throw new ApplicationException("Invalid file", e);
				}
			}
		}
	}
	
	private String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		return invocation.getTarget().getType().getQualifiedName();
	}
}
