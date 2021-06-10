package matcher.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.InterfaceImplementationInstance;
import matcher.entities.MethodInstance;
import matcher.handlers.InstancesCache;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public class ClassProcessor implements Processor<ClassInstance, CtClass<?>>{
	
	private ConflictPattern conflictPattern;

	public ClassProcessor(ConflictPattern conflictPattern) {
		super();
		this.conflictPattern = conflictPattern;
	}
	
	public ClassInstance process(CtClass<?> element, boolean fullyBuild) {
		if(element!= null) {
			if(InstancesCache.getInstance().hasClass(element)) {
				return useCache(element);
			}
			ClassInstance classInstance = 
					new ClassInstance(element.getSimpleName(), element.getQualifiedName());
			if(fullyBuild || conflictPattern.hasSuperClasses())
				processSuperClass(element, classInstance);
			if(fullyBuild || conflictPattern.hasFields())
				processFields(element, classInstance);
			if(fullyBuild || conflictPattern.hasMethods()) {
				List<MethodInstance> methods = processMethods(element, classInstance);
				if(fullyBuild || conflictPattern.hasCompatibleMethods())
					processCompatibleMethods(methods, classInstance);
			}
			if(fullyBuild || conflictPattern.hasConstructors())
				processConstructors(element, classInstance);
			if(fullyBuild || conflictPattern.hasInterfaces())
				processInterfaces(element, classInstance);
			
			InstancesCache.getInstance().putClass(element, classInstance);
			
			return classInstance;
		}
		return null;
	}

	@Override
	public ClassInstance process(CtClass<?> element) {
		return process(element, false);
	}
	
	private ClassInstance useCache(CtClass<?> element) {
		ClassInstance result = InstancesCache.getInstance().getClass(element);
		if(conflictPattern.hasSuperClasses() && !result.hasSuperClass())
			processSuperClass(element, result);
		if(conflictPattern.hasFields() && !result.hasFields())
			processFields(element, result);
		if(conflictPattern.hasMethods()) {
			List<MethodInstance> methods;
			if(!result.hasMethods()) {
				methods = processMethods(element, result);
			}
			else if((conflictPattern.hasInvocations() && !result.hasInvocations()) ||
					conflictPattern.hasFieldAccesses() && !result.hasFieldAccesses()) {
				result.clearMethods();
				methods = processMethods(element, result);
			}
			else {
				methods = result.getMethods();
			}

			if(conflictPattern.hasCompatibleMethods() && !result.hasCompatibles())
				processCompatibleMethods(methods, result);
		}
		if(conflictPattern.hasConstructors() && !result.hasConstructors())
			processConstructors(element, result);
		if(conflictPattern.hasInterfaces() && !result.hasInterfaces())
			processInterfaces(element, result);
		InstancesCache.getInstance().putClass(element, result);
		return new ClassInstance(result);
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
			if(constructorInstance != null) {
				constructorInstance.setClassInstance(classInstance);
				classInstance.addConstructor(constructorInstance);
			}
		}
	}

	private void processFields(CtClass<?> element, ClassInstance classInstance) {
		FieldProcessor fieldProcessor = new FieldProcessor();
		for(CtFieldReference<?> f: element.getDeclaredFields()) {
			FieldInstance field = fieldProcessor.process(f.getFieldDeclaration());
			if(field != null)
				classInstance.addField(field);
		}

	}

	private List<MethodInstance> processMethods(CtClass<?> element, ClassInstance classInstance) {
		MethodProcessor methodProcessor = new MethodProcessor(conflictPattern);
		List<MethodInstance> methods = new ArrayList<>();
		for(CtMethod<?> method: element.getMethods()) {
			MethodInstance methodInstance = methodProcessor.process(method);
			if(methodInstance != null) {
				classInstance.addMethod(methodInstance);
				methods.add(methodInstance);
			}
		}
		return methods;
	}

	private void processSuperClass(CtClass<?> element, ClassInstance classInstance) {
		CtTypeReference<?> superClass = element.getSuperclass();
		if(superClass != null) {
			ClassProcessor superClassProcessor = new ClassProcessor(conflictPattern);
			CtType<?> superType = superClass.getTypeDeclaration();
			ClassInstance superClassInstance =
					superClassProcessor.process((CtClass<?>)superType);
			if(superClassInstance != null)
				classInstance.setSuperClass(superClassInstance);
		}
	}

}
