package matcher.processors;

import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.patterns.ConflictPattern;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.visitor.filter.TypeFilter;

public class ConstructorProcessor extends AbstractProcessor<CtConstructor<?>>{

	private ConstructorInstance constructorInstance;
	private ConflictPattern conflictPattern;
	
	public ConstructorProcessor(ConflictPattern conflicPattern) {
		this.conflictPattern = conflicPattern;
	}
	
	public ConstructorInstance getConstructorInstance() {
		return constructorInstance;
	}

	@Override
	public void process(CtConstructor<?> element) {
		Visibility visibility = Visibility.PACKAGE;
		if(element.getVisibility() != null)
			visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
		List<Type> parameters = element.getParameters().stream()
													   .map(p -> new Type(p.getType()))
													   .collect(Collectors.toList());
		constructorInstance = new ConstructorInstance(visibility, parameters);
		if(conflictPattern.hasInvocations())
			processMethodInvocations(element);
	}

	private void processMethodInvocations(CtConstructor<?> element) {
		List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
		for(CtInvocation<?> invocation: invocations) {
			MethodInvocationInstance mii = 
					new MethodInvocationInstance(getInvocationQualifiedName(invocation));
			constructorInstance.addMethodInvocation(mii);
		}
	}

	private String getInvocationQualifiedName(CtInvocation<?> invocation) {
		return invocation.getTarget().getType() + "." + invocation.getExecutable().getSignature();
	}

}
