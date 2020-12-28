package matcher.processors;

import java.lang.annotation.Annotation;
import java.util.Optional;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldAccessType;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteAction;
import matcher.entities.deltas.DeleteFieldAction;
import matcher.entities.deltas.DeleteMethodAction;
import matcher.patterns.ConflictPattern;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.CtVisitor;

public class DeleteActionsProcessor extends DeltaProcessor implements CtVisitor{
	
	public DeleteActionsProcessor(ConflictPattern conflictPattern) {
		super(conflictPattern);
	}

	@Override
	public <T> void visitCtConstructor(CtConstructor<T> c) {
		if(getConflictPattern().hasDeleteActions()) {
			ClassInstance holderInstance = getClassInstance(c);
			ConstructorInstance insertedInstance = getConstructorInstance(c, holderInstance);
			ActionInstance result = new DeleteAction(insertedInstance, holderInstance);
			setResult(result);
		}
		
	}
	
	@Override
	public <T> void visitCtField(CtField<T> field) {
		if(getConflictPattern().hasDeleteFieldActions()) {
			ClassInstance holderInstance = getClassInstance(field);
			FieldInstance deletedInstance = getFieldInstance(field, holderInstance);
			ActionInstance result = new DeleteFieldAction(deletedInstance, holderInstance, 
					deletedInstance.getVisibility());
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtInvocation(CtInvocation<T> invocation) {
		if(getConflictPattern().hasDeleteActions()) {
			MethodInvocationInstance mii = new MethodInvocationInstance(
					getMethodProcessor().getInvocationQualifiedName(invocation));
			Optional<CtMethod<?>> possibleCaller = getMethodNode(invocation);
			if(possibleCaller.isPresent()) {
				CtMethod<?> method = possibleCaller.get();
				ClassInstance classInstance = getClassInstance(method);
				MethodInstance methodInstance = getMethodInstance(method, classInstance);
				ActionInstance result = new DeleteAction(mii, methodInstance);
				setResult(result);
			}
			else {
				Optional<CtConstructor<?>> constructor = getConstructorNode(invocation);
				if(constructor.isPresent()) {
					CtConstructor<?> c = constructor.get();
					ClassInstance classInstance = getClassInstance(c);
					ConstructorInstance constructorInstance = getConstructorInstance(c, classInstance);
					ActionInstance result = new DeleteAction(mii, constructorInstance);
					setResult(result);
				}
			}
		}
	}
	
	@Override
	public <T> void visitCtMethod(CtMethod<T> method) {
		if(getConflictPattern().hasDeleteMethodActions()) {
			ClassInstance holderInstance = getClassInstance(method);
			MethodInstance deletedInstance = getMethodInstance(method, holderInstance);
			ActionInstance result =  new DeleteMethodAction(deletedInstance, holderInstance, 
					deletedInstance.getVisibility());
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
		if(getConflictPattern().hasDeleteActions()) {
			String fieldQualifiedName = getMethodProcessor()
					.getFieldQualifiedName(fieldRead.getVariable());
			FieldAccessInstance fai = new FieldAccessInstance(fieldQualifiedName, FieldAccessType.READ);
			Optional<CtMethod<?>> possibleCaller = getMethodNode(fieldRead);
			if(possibleCaller.isPresent()) {
				CtMethod<?> method = possibleCaller.get();
				ClassInstance classInstance = getClassInstance(method);
				MethodInstance methodInstance = getMethodInstance(method, classInstance);
				ActionInstance result = new DeleteAction(fai, methodInstance);
				setResult(result);
			}
		}
	}

	@Override
	public <T> void visitCtFieldWrite(CtFieldWrite<T> fieldWrite) {
		if(getConflictPattern().hasDeleteActions()) {
			String fieldQualifiedName = getMethodProcessor()
					.getFieldQualifiedName(fieldWrite.getVariable());
			FieldAccessInstance fai = new FieldAccessInstance(fieldQualifiedName, FieldAccessType.WRITE);
			Optional<CtMethod<?>> possibleCaller = getMethodNode(fieldWrite);
			if(possibleCaller.isPresent()) {
				CtMethod<?> method = possibleCaller.get();
				ClassInstance classInstance = getClassInstance(method);
				MethodInstance methodInstance = getMethodInstance(method, classInstance);
				ActionInstance result = new DeleteAction(fai, methodInstance);
				setResult(result);
			}
		}
	}
	

	@Override
	public <A extends Annotation> void visitCtAnnotation(CtAnnotation<A> annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtCodeSnippetExpression(CtCodeSnippetExpression<T> expression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtCodeSnippetStatement(CtCodeSnippetStatement statement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <A extends Annotation> void visitCtAnnotationType(CtAnnotationType<A> annotationType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtAnonymousExecutable(CtAnonymousExecutable anonymousExec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtArrayRead(CtArrayRead<T> arrayRead) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtArrayWrite(CtArrayWrite<T> arrayWrite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtArrayTypeReference(CtArrayTypeReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtAssert(CtAssert<T> asserted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T, A extends T> void visitCtAssignment(CtAssignment<T, A> assignement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <R> void visitCtBlock(CtBlock<R> block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtBreak(CtBreak breakStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S> void visitCtCase(CtCase<S> caseStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtCatch(CtCatch catchBlock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtTypeParameter(CtTypeParameter typeParameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtConditional(CtConditional<T> conditional) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtContinue(CtContinue continueStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtDo(CtDo doLoop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Enum<?>> void visitCtEnum(CtEnum<T> ctEnum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtExecutableReference(CtExecutableReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtEnumValue(CtEnumValue<T> enumValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtThisAccess(CtThisAccess<T> thisAccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtFieldReference(CtFieldReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtUnboundVariableReference(CtUnboundVariableReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtFor(CtFor forLoop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtForEach(CtForEach foreach) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtIf(CtIf ifElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtInterface(CtInterface<T> intrface) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> literal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtLocalVariableReference(CtLocalVariableReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtCatchVariable(CtCatchVariable<T> catchVariable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtCatchVariableReference(CtCatchVariableReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtAnnotationMethod(CtAnnotationMethod<T> annotationMethod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtNewArray(CtNewArray<T> newArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtConstructorCall(CtConstructorCall<T> ctConstructorCall) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtNewClass(CtNewClass<T> newClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtLambda(CtLambda<T> lambda) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T, E extends CtExpression<?>> void visitCtExecutableReferenceExpression(
			CtExecutableReferenceExpression<T, E> expression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T, A extends T> void visitCtOperatorAssignment(CtOperatorAssignment<T, A> assignment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtPackage(CtPackage ctPackage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtPackageReference(CtPackageReference reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtParameter(CtParameter<T> parameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtParameterReference(CtParameterReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <R> void visitCtReturn(CtReturn<R> returnStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <R> void visitCtStatementList(CtStatementList statements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S> void visitCtSwitch(CtSwitch<S> switchStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T, S> void visitCtSwitchExpression(CtSwitchExpression<T, S> switchExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtSynchronized(CtSynchronized synchro) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtThrow(CtThrow throwStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtTry(CtTry tryBlock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtTryWithResource(CtTryWithResource tryWithResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtTypeParameterReference(CtTypeParameterReference ref) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtWildcardReference(CtWildcardReference wildcardReference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtIntersectionTypeReference(CtIntersectionTypeReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtTypeReference(CtTypeReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtTypeAccess(CtTypeAccess<T> typeAccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtVariableWrite(CtVariableWrite<T> variableWrite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtWhile(CtWhile whileLoop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtAnnotationFieldAccess(CtAnnotationFieldAccess<T> annotationFieldAccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtSuperAccess(CtSuperAccess<T> f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtComment(CtComment comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtJavaDoc(CtJavaDoc comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtJavaDocTag(CtJavaDocTag docTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtImport(CtImport ctImport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtModule(CtModule module) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtModuleReference(CtModuleReference moduleReference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtPackageExport(CtPackageExport moduleExport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtModuleRequirement(CtModuleRequirement moduleRequirement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtProvidedService(CtProvidedService moduleProvidedService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtUsedService(CtUsedService usedService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtCompilationUnit(CtCompilationUnit compilationUnit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtPackageDeclaration(CtPackageDeclaration packageDeclaration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtTypeMemberWildcardImportReference(CtTypeMemberWildcardImportReference wildcardReference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtYieldStatement(CtYieldStatement statement) {
		// TODO Auto-generated method stub
		
	}

}
