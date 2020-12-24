package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<CtClass<?>> getInvolvedClasses(CtClass<?> changedClass) throws ApplicationException{
		List<CtInvocation<?>> invocations = 
				changedClass.getElements(new TypeFilter(CtInvocation.class));
		invocations = invocations.stream()
								 .filter(i -> !i.toString().equals("super()"))
								 .collect(Collectors.toList());
		List<CtClass<?>> result = new ArrayList<>();
		result.add(changedClass);
		for(CtInvocation<?> i: invocations) {
			Optional<CtClass<?>> invoked = getClass(i);
			if(invoked.isPresent()) {
				CtClass<?> actualInvoked = invoked.get();
				if(!actualInvoked.equals(changedClass) &&
				   !changedClass.getReference().isSubtypeOf(actualInvoked.getReference()) &&
				   !result.contains(actualInvoked)) {
					result.add(actualInvoked);
				}
			}
		}
		return result;
	}
	
	private Optional<CtClass<?>> getClass(CtInvocation<?> invocation) throws ApplicationException{
		String simpleName = invocation.getExecutable().getDeclaringType().getSimpleName();
		Optional<File> srcFile = FileSystemHandler.getInstance().getSrcFile(simpleName + ".java");
		if(srcFile.isPresent()) {
			SpoonResource resource = null;
			try {
				resource = SpoonResourceHelper.createResource(srcFile.get());
			} catch (FileNotFoundException e) {
				throw new ApplicationException("Invalid src file", e);
			}
			return Optional.ofNullable((CtClass<?>) new AstComparator().getCtType(resource));
			
		}
		return Optional.empty();
	}
}
