package matcher.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.patterns.ConflictPattern;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public class ClassProcessor extends AbstractProcessor<CtClass<?>>{

	private ClassInstance classInstance;
	private ConflictPattern conflictPattern;
		
	public ClassProcessor(ConflictPattern conflictPattern) {
		super();
		this.conflictPattern = conflictPattern;
	}
	
	public ClassInstance getClassInstance() {
		return classInstance;
	}

	@Override
	public void process(CtClass<?> element) {
		classInstance = new ClassInstance(element.getSimpleName(), element.getQualifiedName());
		if(conflictPattern.hasSuperClasses())
			processSuperClass(element);
		if(conflictPattern.hasFields())
			processFields(element);
		if(conflictPattern.hasMethods()) {
			List<MethodInstance> methods = processMethods(element);
			if(conflictPattern.hasCompatibleMethods())
				processCompatibleMethods(methods);
		}
		if(conflictPattern.hasConstructors())
			processConstructors(element);
	}

	private void processCompatibleMethods(List<MethodInstance> methods) {
		for(MethodInstance method: methods) {
			List<MethodInstance> sameNameMethods = methods.stream()
					.filter(m -> m.getName().equals(method.getName()))
					.collect(Collectors.toList());
			for(MethodInstance other: sameNameMethods) {
				if(!method.equals(other) && method.isCompatibleWith(other)) {
					classInstance.addCompatible(method, other);
				}
			}
		}
	}

	private void processConstructors(CtClass<?> element) {
		ConstructorProcessor constructorProcessor = new ConstructorProcessor(conflictPattern);
		for(CtConstructor<?> constructor: element.getConstructors()) {
			constructorProcessor.process(constructor);
			ConstructorInstance constructorInstance = constructorProcessor.getConstructorInstance();
			constructorInstance.setClassInstance(getClassInstance());
			classInstance.addConstructor(constructorInstance);
		}
	}

	private void processFields(CtClass<?> element) {
		FieldProcessor fieldProcessor = new FieldProcessor();
		for(CtFieldReference<?> f: element.getAllFields()) {
			fieldProcessor.process(f.getFieldDeclaration());
			FieldInstance field = fieldProcessor.getFieldInstance();
			field.setClassInstance(classInstance);
			classInstance.addField(field);
		}
		
	}

	private List<MethodInstance> processMethods(CtClass<?> element) {
		MethodProcessor methodProcessor = new MethodProcessor(conflictPattern);
		List<MethodInstance> methods = new ArrayList<>();
		for(CtMethod<?> method: element.getMethods()) {
			methodProcessor.process(method);
			MethodInstance methodInstance = methodProcessor.getMethodInstance();
			methodInstance.setClassInstance(getClassInstance());
			classInstance.addMethod(methodInstance);
			methods.add(methodInstance);
		}
		return methods;
	}

	private void processSuperClass(CtClass<?> element) {
		CtTypeReference<?> superClass = element.getSuperclass();
		if(superClass != null) {
			ClassProcessor superClassProcessor = new ClassProcessor(conflictPattern);
			CtType<?> superType = superClass.getTypeDeclaration();
			superClassProcessor.process((CtClass<?>)superType);
			classInstance.setSuperClass(superClassProcessor.getClassInstance());
		}
	}

}
