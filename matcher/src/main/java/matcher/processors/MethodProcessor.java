package matcher.processors;

import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
import matcher.handlers.FileSystemHandler;
import matcher.patterns.ConflictPattern;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodProcessor extends AbstractProcessor<CtMethod<?>>{

	private MethodInstance methodInstance;
	private ConflictPattern conflictPattern;

	public MethodProcessor(ConflictPattern conflictPattern) {
		this.conflictPattern = conflictPattern;
	}

	public MethodInstance getMethodInstance() {
		return methodInstance;
	}

	@Override
	public void process(CtMethod<?> element) {
		Visibility visibility = Visibility.PACKAGE;
		if(element.getVisibility() != null)
			visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
		List<Type> parameters = element.getParameters().stream()
				.map(p -> new Type(p.getType()))
				.collect(Collectors.toList());
		Type returnType = new Type(element.getType());
		methodInstance = new MethodInstance(element.getSimpleName(), visibility, returnType, parameters);
		if(conflictPattern.hasInvocations())
			processInvocations(element);
		if(conflictPattern.hasFieldAccesses())
			processFieldAccesses(element);
	}

	private void processFieldAccesses(CtMethod<?> element) {
		processFieldReads(element);
		processFieldWrites(element);
	}

	private void processFieldWrites(CtMethod<?> element) {
		List<CtFieldWrite<?>> fieldWrites = element.getElements(new TypeFilter(CtFieldWrite.class));
		for(CtFieldWrite<?> fieldWrite: fieldWrites) {
			String qualifiedName = getFieldQualifiedName(fieldWrite.getVariable());
			FieldAccessInstance access = new FieldAccessInstance(qualifiedName, FieldAccessType.WRITE);
			methodInstance.addFieldAccess(access);
		}

	}

	private void processFieldReads(CtMethod<?> element) {
		List<CtFieldRead<?>> fieldReads = element.getElements(new TypeFilter(CtFieldRead.class));
		for(CtFieldRead<?> fieldRead: fieldReads) {
			String qualifiedName = getFieldQualifiedName(fieldRead.getVariable());
			FieldAccessInstance access = new FieldAccessInstance(qualifiedName, FieldAccessType.READ);
			methodInstance.addFieldAccess(access);
		}
	}

	private void processInvocations(CtMethod<?> element) {
		List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
		for(CtInvocation<?> invocation: invocations) {
			if(!invocation.toString().equals("super()")){
				String invocationClassName = getInvocationClassName(invocation) + ".class";
				try {
					if(FileSystemHandler.getInstance().fromTheSystem(invocationClassName)) {
						MethodInvocationInstance mii = 
								new MethodInvocationInstance(getInvocationQualifiedName(invocation));
						methodInstance.addMethodInvocation(mii);
					}
				} catch (ApplicationException e) {}
			}
		}
	}

	public String getInvocationQualifiedName(CtInvocation<?> invocation) {
		return invocation.getTarget().getType() + "." + invocation.getExecutable().getSignature();
	}

	private String getInvocationClassName(CtInvocation<?> invocation) {
		return invocation.getTarget().getType().getSimpleName();
	}

	public String getFieldQualifiedName(CtFieldReference<?> field) {
		String fieldName = field.getSimpleName();
		String classQualifiedName =  field.getFieldDeclaration().getTopLevelType().getQualifiedName();
		return classQualifiedName + "." + fieldName;
	}

}
