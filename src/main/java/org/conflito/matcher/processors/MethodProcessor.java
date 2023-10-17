package org.conflito.matcher.processors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.FieldAccessType;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.Type;
import org.conflito.matcher.entities.Visibility;
import org.conflito.matcher.handlers.InstancesCache;
import org.conflito.matcher.handlers.SpoonHandler;
import org.conflito.matcher.patterns.ConflictPattern;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodProcessor implements Processor<MethodInstance, CtMethod<?>>{

	private ConflictPattern conflictPattern;

	public MethodProcessor(ConflictPattern conflictPattern) {
		this.conflictPattern = conflictPattern;
	}

	@Override
	public MethodInstance process(CtMethod<?> element) {
		if(element == null)
			return null;
		Set<String> invocationsVisited = new HashSet<>();
		
		if(InstancesCache.getInstance().hasMethod(element)) {
			MethodInstance result = InstancesCache.getInstance().getMethod(element);
			if(conflictPattern.hasInvocations() && !result.hasDependencies()) {
				processInvocations(element, result, invocationsVisited);
				InstancesCache.getInstance().putMethod(element, result);
			}
			if(conflictPattern.hasFieldAccesses() && !result.hasFieldAccesses()) {
				processFieldAccesses(element, result);
				InstancesCache.getInstance().putMethod(element, result);
			}		
			return new MethodInstance(result);
		}
		return process(element, invocationsVisited);
	}
	
	private MethodInstance process(CtMethod<?> element, Set<String> invocationsVisited) {
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
			processInvocations(element, methodInstance, invocationsVisited);
		if(conflictPattern.hasFieldAccesses())
			processFieldAccesses(element, methodInstance);
		InstancesCache.getInstance().putMethod(element, methodInstance);
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
	private void processInvocations(CtMethod<?> element, MethodInstance methodInstance, 
			Set<String> invocationsVisited) {
		List<CtInvocation<?>> invocations = element.getElements(new TypeFilter(CtInvocation.class));
		MethodProcessor invocationsProcessor = new MethodProcessor(conflictPattern);
		for(CtInvocation<?> invocation: invocations) {
			if(SpoonHandler.validInvocation(invocation)){
				try {
					if(SpoonHandler.invocationOfObjectMethod(invocation) || 
							SpoonHandler.invocationFromTheSystem(invocation)) {
						String invocationFullName = 
								SpoonHandler.getInvocationFullName(invocation);
						if(!invocationsVisited.contains(invocationFullName)) {
							invocationsVisited.add(invocationFullName);
							CtMethod<?> invokedMethod = 
									SpoonHandler.getMethodFromInvocation(invocation);
							if(InstancesCache.getInstance().hasMethod(invokedMethod)) {
								methodInstance.addDirectDependency(
										InstancesCache.getInstance().getMethod(invokedMethod));
							}
							else {
								methodInstance.addDirectDependency(
										invocationsProcessor.process(invokedMethod, invocationsVisited));
							}
						}
					}
				} catch (Exception e) {}
			}
		}
	}
}
