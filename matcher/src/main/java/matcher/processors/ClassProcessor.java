package matcher.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.InterfaceImplementationInstance;
import matcher.entities.MethodInstance;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public class ClassProcessor extends Processor<ClassInstance, CtClass<?>>{
	
	private ConflictPattern conflictPattern;

	public ClassProcessor(ConflictPattern conflictPattern) {
		super();
		this.conflictPattern = conflictPattern;
	}

	@Override
	public ClassInstance process(CtClass<?> element) {
		if(element!= null) {
			ClassInstance classInstance = 
					new ClassInstance(element.getSimpleName(), element.getQualifiedName());
			if(conflictPattern.hasSuperClasses())
				processSuperClass(element, classInstance);
			if(conflictPattern.hasFields())
				processFields(element, classInstance);
			if(conflictPattern.hasMethods()) {
				List<MethodInstance> methods = processMethods(element, classInstance);
				if(conflictPattern.hasCompatibleMethods())
					processCompatibleMethods(methods, classInstance);
			}
			if(conflictPattern.hasConstructors())
				processConstructors(element, classInstance);
			if(conflictPattern.hasInterfaces())
				processInterfaces(element, classInstance);
			return classInstance;
		}
		return null;
	}

	private void processInterfaces(CtClass<?> element, ClassInstance classInstance) {
		element.getSuperInterfaces().forEach(i -> {
			classInstance.addInterface(new InterfaceImplementationInstance(i.getQualifiedName()));

		});
	}

	private void processCompatibleMethods(List<MethodInstance> methods, 
			ClassInstance classInstance) {
		for(MethodInstance method: methods) {
			List<MethodInstance> sameNameMethods = methods.stream()
					.filter(m -> !method.equals(m) && m.getName().equals(method.getName()))
					.collect(Collectors.toList());
			for(MethodInstance other: sameNameMethods) {
				if(method.isCompatibleWith(other)) {
					classInstance.addCompatible(method, other);
				}
			}
		}
	}

	private void processConstructors(CtClass<?> element, ClassInstance classInstance) {
		ConstructorProcessor constructorProcessor = new ConstructorProcessor(conflictPattern);
		for(CtConstructor<?> constructor: element.getConstructors()) {
			ConstructorInstance constructorInstance = constructorProcessor.process(constructor);
			constructorInstance.setClassInstance(classInstance);
			classInstance.addConstructor(constructorInstance);
		}
	}

	private void processFields(CtClass<?> element, ClassInstance classInstance) {
		FieldProcessor fieldProcessor = new FieldProcessor();
		for(CtFieldReference<?> f: element.getDeclaredFields()) {
			FieldInstance field = fieldProcessor.process(f.getFieldDeclaration());
			classInstance.addField(field);
		}

	}

	private List<MethodInstance> processMethods(CtClass<?> element, ClassInstance classInstance) {
		MethodProcessor methodProcessor = new MethodProcessor(conflictPattern);
		List<MethodInstance> methods = new ArrayList<>();
		for(CtMethod<?> method: element.getMethods()) {
			MethodInstance methodInstance = methodProcessor.process(method);
			classInstance.addMethod(methodInstance);
			methods.add(methodInstance);
		}
		return methods;
	}

	private void processSuperClass(CtClass<?> element, ClassInstance classInstance) {
		CtTypeReference<?> superClass = element.getSuperclass();
		if(superClass != null) {
			ClassProcessor superClassProcessor = new ClassProcessor(conflictPattern);
			CtType<?> superType = superClass.getTypeDeclaration();
			classInstance.setSuperClass(superClassProcessor.process((CtClass<?>)superType));
		}
	}

}
