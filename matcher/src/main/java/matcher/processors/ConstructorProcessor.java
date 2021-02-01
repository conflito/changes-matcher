package matcher.processors;

import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ConstructorInstance;
import matcher.entities.MethodInstance;
import matcher.entities.Type;
import matcher.entities.Visibility;
import matcher.handlers.InstancesCache;
import matcher.handlers.SpoonHandler;
import matcher.patterns.ConflictPattern;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class ConstructorProcessor extends Processor<ConstructorInstance, CtConstructor<?>>{

	private ConflictPattern conflictPattern;
	
	public ConstructorProcessor(ConflictPattern conflicPattern) {
		this.conflictPattern = conflicPattern;
	}

	@Override
	public ConstructorInstance process(CtConstructor<?> element) {
		if(InstancesCache.getInstance().hasConstructor(element))
			return InstancesCache.getInstance().getConstructor(element);
		Visibility visibility = Visibility.PACKAGE;
		if(element.getVisibility() != null)
			visibility = Visibility.valueOf(element.getVisibility().toString().toUpperCase());
		List<Type> parameters = element.getParameters().stream()
													   .map(p -> new Type(p.getType()))
													   .collect(Collectors.toList());
		ConstructorInstance constructorInstance = new ConstructorInstance(visibility, parameters);
		if(conflictPattern.hasInvocations())
			processMethodInvocations(element, constructorInstance);
		
		InstancesCache.getInstance().putConstructor(element, constructorInstance);
		
		return constructorInstance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processMethodInvocations(CtConstructor<?> element
			, ConstructorInstance constructorInstance) {
		List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
		for(CtInvocation<?> invocation: invocations) {
			if(!invocation.toString().equals("super()")) {
				try {
					if(SpoonHandler.invocationFromTheSystem(invocation)) {
						MethodInstance invoked = 
								new MethodProcessor(conflictPattern).process(getMethodFromInvocation(invocation));
						constructorInstance.addDirectDependency(invoked);
					}
				} catch (Exception e) {}

			}
		}
	}
	
	private CtMethod<?> getMethodFromInvocation(CtInvocation<?> invocation){
		String invokedName = invocation.getExecutable().getSimpleName();
		CtTypeReference<?>[] types = invocation.getExecutable().getParameters()
				.toArray(new CtTypeReference<?>[0]);
		return invocation.getExecutable()
			.getDeclaringType()
			.getTypeDeclaration()
			.getMethod(invokedName, types);
	}

}
