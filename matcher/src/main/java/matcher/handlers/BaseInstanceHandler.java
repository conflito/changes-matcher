package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gumtree.spoon.AstComparator;
import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.ClassProcessor;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

public class BaseInstanceHandler {
	
	public BaseInstance getBaseInstance(File base, ConflictPattern cp) throws ApplicationException {
		SpoonResource resource = null;
		try {
			resource = SpoonResourceHelper.createFile(base);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid specified file", e);
		}
		CtClass<?> changedClass = (CtClass<?>) new AstComparator().getCtType(resource);
		BaseInstance result = new BaseInstance();
		for(CtClass<?> c: getInvolvedClasses(changedClass)) {
			ClassProcessor processor = new ClassProcessor(cp);
			processor.process(c);
			result.addClassInstance(processor.getClassInstance());
		}
		return result;
	}
	
	private List<CtClass<?>> getInvolvedClasses(CtClass<?> changedClass){
		List<CtInvocation<?>> invocations = 
				changedClass.getElements(new TypeFilter(CtInvocation.class));
		invocations = invocations.stream()
								 .filter(i -> !i.toString().equals("super()"))
								 .collect(Collectors.toList());
		List<CtClass<?>> result = new ArrayList<>();
		result.add(changedClass);
		for(CtInvocation<?> i: invocations) {
			CtClass<?> invokedClass = (CtClass<?>) i.getExecutable()
													.getDeclaringType().getTypeDeclaration();
			if(!invokedClass.equals(changedClass) && 
			   !changedClass.getReference().isSubtypeOf(invokedClass.getReference()) &&
			   !isFromJDK(invokedClass) &&
			   !result.contains(invokedClass)) {
				result.add(invokedClass);
			}
		}
		return result;
	}

	private boolean isFromJDK(CtClass<?> invokedClass) {
		try {
			Class<?> invoked = Class.forName(invokedClass.getQualifiedName());
			return "".getClass().getClassLoader() == invoked.getClassLoader();
		} catch( ClassNotFoundException e ) {
			return false;
		}
	}
}
