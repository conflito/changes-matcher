package matcher.processors;

import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ConstructorInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.handlers.FileSystemHandler;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processMethodInvocations(CtConstructor<?> element) {
		List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
		for(CtInvocation<?> invocation: invocations) {
			if(!invocation.toString().equals("super()")) {
				try {
					String invocationClassName = getInvocationClassSimpleName(invocation) + ".java";
					if(FileSystemHandler.getInstance().fromTheSystem(invocationClassName)) {
						constructorInstance.addDirectDependencyName(
								getInvocationClassQualifiedName(invocation)
								+ "." + getInvocationQualifiedName(invocation));
					}
				} catch (Exception e) {}

			}
		}
	}

	private String getInvocationQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getSignature().replace(",", ", ");
	}
	
	private String getInvocationClassSimpleName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getSimpleName();
	}
	
	private String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getQualifiedName();
	}

}
