package matcher.processors;

import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;
import matcher.entities.MethodInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.handlers.FileSystemHandler;
import matcher.patterns.ConflictPattern;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodProcessor extends Processor<MethodInstance, CtMethod<?>>{

	private ConflictPattern conflictPattern;

	public MethodProcessor(ConflictPattern conflictPattern) {
		this.conflictPattern = conflictPattern;
	}

	@Override
	public MethodInstance process(CtMethod<?> element) {
		Visibility visibility = Visibility.PACKAGE;
		if(element.getVisibility() != null)
			visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
		List<Type> parameters = element.getParameters().stream()
				.map(p -> new Type(p.getType()))
				.collect(Collectors.toList());
		Type returnType = new Type(element.getType());
		MethodInstance methodInstance = 
				new MethodInstance(element.getSimpleName(), visibility, returnType, parameters);
		if(conflictPattern.hasInvocations())
			processInvocations(element, methodInstance);
		if(conflictPattern.hasFieldAccesses())
			processFieldAccesses(element, methodInstance);
		return methodInstance;
	}

	private void processFieldAccesses(CtMethod<?> element, MethodInstance methodInstance) {
		processFieldReads(element, methodInstance);
		processFieldWrites(element, methodInstance);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processFieldWrites(CtMethod<?> element, MethodInstance methodInstance) {
		List<CtFieldWrite<?>> fieldWrites = element.getElements(new TypeFilter(CtFieldWrite.class));
		for(CtFieldWrite<?> fieldWrite: fieldWrites) {
			String qualifiedName = fieldWrite.getVariable().getSimpleName();
			FieldAccessInstance access = new FieldAccessInstance(qualifiedName, FieldAccessType.WRITE);
			methodInstance.addFieldAccess(access);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processFieldReads(CtMethod<?> element, MethodInstance methodInstance) {
		List<CtFieldRead<?>> fieldReads = element.getElements(new TypeFilter(CtFieldRead.class));
		for(CtFieldRead<?> fieldRead: fieldReads) {
			String qualifiedName = fieldRead.getVariable().getSimpleName();
			FieldAccessInstance access = new FieldAccessInstance(qualifiedName, FieldAccessType.READ);
			methodInstance.addFieldAccess(access);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processInvocations(CtMethod<?> element, MethodInstance methodInstance) {
		List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
		for(CtInvocation<?> invocation: invocations) {
			if(!invocation.toString().equals("super()")){
				try {
					String invocationSrcName = getInvocationClassSimpleName(invocation) + ".java";
					if(FileSystemHandler.getInstance().fromTheSystem(invocationSrcName)) {
						methodInstance.addDirectDependencyName(
								getInvocationClassQualifiedName(invocation)
								+ "." + getInvocationQualifiedName(invocation));
					}
				} catch (Exception e) {}
			}
		}
	}

	public String getInvocationQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getSignature().replace(",", ", ");
	}

	private String getInvocationClassSimpleName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getSimpleName();
	}
	
	private String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getQualifiedName();
	}

	public String getFieldQualifiedName(CtFieldReference<?> field) {
		String fieldName = field.getSimpleName();
		String classQualifiedName =  field.getFieldDeclaration().getTopLevelType().getQualifiedName();
		return classQualifiedName + "." + fieldName;
	}

}
