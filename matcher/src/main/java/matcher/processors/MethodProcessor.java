package matcher.processors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodProcessor extends Processor<MethodInstance, CtMethod<?>>{

	private ConflictPattern conflictPattern;

	public MethodProcessor(ConflictPattern conflictPattern) {
		this.conflictPattern = conflictPattern;
	}

	@Override
	public MethodInstance process(CtMethod<?> element) {
		Set<String> invocationsVisited = new HashSet<>();
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
			if(!invocation.toString().equals("super()")){
				try {
					String invocationSrcName = getInvocationClassSimpleName(invocation) + ".java";
					if(FileSystemHandler.getInstance().fromTheSystem(invocationSrcName)) {
						String invocationFullName = getInvocationClassQualifiedName(invocation)
								+ "." + getInvocationQualifiedName(invocation);
						if(!invocationsVisited.contains(invocationFullName)) {
							invocationsVisited.add(invocationFullName);
							CtMethod<?> invokedMethod = getMethodFromInvocation(invocation);
							methodInstance.addDirectDependency(
									invocationsProcessor.process(invokedMethod, 
											invocationsVisited));
						}
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
