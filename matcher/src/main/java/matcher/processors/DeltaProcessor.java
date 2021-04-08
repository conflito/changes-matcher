package matcher.processors;

import java.util.Optional;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.deltas.ActionInstance;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtTypeMember;
import spoon.reflect.visitor.filter.TypeFilter;

public abstract class DeltaProcessor {

	private ConflictPattern conflictPattern;
	
	private ClassProcessor classProcessor;
	private MethodProcessor methodProcessor;
	private ConstructorProcessor constructorProcessor;
	private FieldProcessor fieldProcessor;
	
	private ActionInstance result;

	public DeltaProcessor(ConflictPattern conflictPattern) {
		super();
		this.conflictPattern = conflictPattern;
		classProcessor = new ClassProcessor(conflictPattern);
		methodProcessor = new MethodProcessor(conflictPattern);
		constructorProcessor = new ConstructorProcessor(conflictPattern);
		fieldProcessor = new FieldProcessor();
	}
	
	public Optional<ActionInstance> getResult() {
		return Optional.ofNullable(result);
	}
	
	protected ConflictPattern getConflictPattern() {
		return this.conflictPattern;
	}
	
	protected MethodProcessor getMethodProcessor() {
		return methodProcessor;
	}

	protected void setResult(ActionInstance result) {
		this.result = result;
	}
	
	protected ClassInstance getClassInstance(CtTypeMember member) {
		CtClass<?> holder = (CtClass<?>) member.getTopLevelType();
		return getClassInstance(holder);
	}
	
	protected ClassInstance getClassInstance(CtClass<?> c) {
		return classProcessor.process(c);
	}
	
	protected ClassInstance getClassInstance(CtClass<?> c, boolean fullyBuild) {
		return classProcessor.process(c, fullyBuild);
	}
	
	protected FieldInstance getFieldInstance(CtField<?> field) {
		return fieldProcessor.process(field);
	}
	
	protected ConstructorInstance getConstructorInstance(CtConstructor<?> constructor, 
			ClassInstance classInstance) {
		ConstructorInstance constructorInstance = constructorProcessor.process(constructor);
		constructorInstance.setClassInstance(classInstance);
		return constructorInstance;
	}
	
	protected MethodInstance getMethodInstance(CtMethod<?> method) {
		return methodProcessor.process(method);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Optional<CtConstructor<?>> getConstructorNode(CtElement node) {
		return Optional.ofNullable((CtConstructor<?>) 
							node.getParent(new TypeFilter(CtConstructor.class)));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Optional<CtMethod<?>> getMethodNode(CtElement node) {
		return Optional.ofNullable((CtMethod<?>) 
							node.getParent(new TypeFilter(CtMethod.class)));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Optional<CtField<?>> getFieldNode(CtElement node){
		return Optional.ofNullable((CtField<?>) 
							node.getParent(new TypeFilter(CtField.class)));
	}
}
