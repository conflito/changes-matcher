package matcher.processors;

import java.lang.annotation.Annotation;
import java.util.Optional;

import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.UpdateConstructorAction;
import matcher.entities.deltas.UpdateFieldTypeAction;
import matcher.entities.deltas.UpdateInvocationAction;
import matcher.entities.deltas.UpdateMethodAction;
import matcher.handlers.SpoonHandler;
import matcher.patterns.ConflictPattern;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.CtVisitor;

public class UpdateActionsProcessor extends DeltaProcessor implements CtVisitor{
		
	private CtElement newOne;
	
	public UpdateActionsProcessor(ConflictPattern conflictPattern, CtElement newOne) {
		super(conflictPattern);
		this.newOne = newOne;
	}
	
	@Override
	public <T> void visitCtMethod(CtMethod<T> method) {
		if(getConflictPattern().hasUpdateActions()) {
			MethodInstance methodInstance = getMethodInstance(method);
			ActionInstance result = new UpdateMethodAction(methodInstance);
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtConstructor(CtConstructor<T> c) {
		if(getConflictPattern().hasUpdateActions()) {
			ClassInstance holderInstance = getClassInstance(c);
			ConstructorInstance cInstance = getConstructorInstance(c, holderInstance);
			ActionInstance result = new UpdateConstructorAction(cInstance);
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtField(CtField<T> f) {
		if(getConflictPattern().hasUpdateFieldTypeActions()) {
			Optional<CtField<?>> newF = getFieldNode(newOne);
			if(newF.isPresent()) {
				FieldInstance fieldInstance = getFieldInstance(f);
				FieldInstance newFieldInstance = getFieldInstance(newF.get());
				ActionInstance result = 
						new UpdateFieldTypeAction(fieldInstance, newFieldInstance.getType());
				setResult(result);
			}
			
		}
	}
	
	@Override
	public <T> void visitCtInvocation(CtInvocation<T> invocation) {
		if(getConflictPattern().hasUpdateInvocationActions()) {
			visitInvocationUpdate(invocation);
		}
		else {
			visit(invocation);
		}
		
	}
	
	private void visitInvocationUpdate(CtInvocation<?> invocation) {
		CtMethod<?> oldInvocation = SpoonHandler.getMethodFromInvocation(invocation);
		CtMethod<?> newInvocation = SpoonHandler.getMethodFromInvocation((CtInvocation<?>)newOne);
		MethodInstance oldDependency = getMethodInstance(oldInvocation);
		MethodInstance newDependency = getMethodInstance(newInvocation);
		Optional<CtMethod<?>> possibleCaller = getMethodNode(invocation);
		if(possibleCaller.isPresent()) {
			Optional<CtMethod<?>> newPossibleCaller = getMethodNode(newOne);
			if(newPossibleCaller.isPresent()) {
				CtMethod<?> oldMethod = possibleCaller.get();
				CtMethod<?> newMethod = newPossibleCaller.get();
				MethodInstance oldMethodInstance = getMethodInstance(oldMethod);
				MethodInstance newMethodInstance = getMethodInstance(newMethod);
				ActionInstance result = 
						new UpdateInvocationAction(oldMethodInstance, newMethodInstance, 
								oldDependency, newDependency);
				setResult(result);
			}

		}
		else {
			Optional<CtConstructor<?>> constructor = getConstructorNode(invocation);
			if(constructor.isPresent()) {
				Optional<CtConstructor<?>> newConstructor = getConstructorNode(invocation);
				if(newConstructor.isPresent()) {
					CtConstructor<?> c = constructor.get();
					CtConstructor<?> nC = newConstructor.get();
					ClassInstance classInstance = getClassInstance(c);
					ConstructorInstance cInstance = getConstructorInstance(c, classInstance);
					ConstructorInstance nCInstance = getConstructorInstance(nC, classInstance);
					ActionInstance result = 
							new UpdateInvocationAction(cInstance, nCInstance, 
									oldDependency, newDependency);
					setResult(result);
				}
				
			}
		}
	}

	private void visit(CtElement element) {
		Optional<CtMethod<?>> method = getMethodNode(element);
		if(method.isPresent()) {
			method.get().accept(this);
		}
		else {
			Optional<CtConstructor<?>> c = getConstructorNode(element);
			if(c.isPresent()) {
				c.get().accept(this);
			}
			else {
				Optional<CtField<?>> f = getFieldNode(element);
				if(f.isPresent()) {
					f.get().accept(this);
				}
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
		visit(anonymousExec);
	}

	@Override
	public <T> void visitCtArrayRead(CtArrayRead<T> arrayRead) {
		visit(arrayRead);
	}

	@Override
	public <T> void visitCtArrayWrite(CtArrayWrite<T> arrayWrite) {
		visit(arrayWrite);
		
	}

	@Override
	public <T> void visitCtArrayTypeReference(CtArrayTypeReference<T> reference) {
		visit(reference);
	}

	@Override
	public <T> void visitCtAssert(CtAssert<T> asserted) {
		visit(asserted);
	}

	@Override
	public <T, A extends T> void visitCtAssignment(CtAssignment<T, A> assignement) {
		visit(assignement);
	}

	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		visit(operator);
	}

	@Override
	public <R> void visitCtBlock(CtBlock<R> block) {
		visit(block);		
	}

	@Override
	public void visitCtBreak(CtBreak breakStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S> void visitCtCase(CtCase<S> caseStatement) {
		visit(caseStatement);
	}

	@Override
	public void visitCtCatch(CtCatch catchBlock) {
		visit(catchBlock);
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
		visit(conditional);
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
		visit(thisAccess);
	}

	@Override
	public <T> void visitCtFieldReference(CtFieldReference<T> reference) {
		visit(reference);		
	}

	@Override
	public <T> void visitCtUnboundVariableReference(CtUnboundVariableReference<T> reference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtFor(CtFor forLoop) {
		visit(forLoop);
	}

	@Override
	public void visitCtForEach(CtForEach foreach) {
		visit(foreach);
		
	}

	@Override
	public void visitCtIf(CtIf ifElement) {
		visit(ifElement);
	}

	@Override
	public <T> void visitCtInterface(CtInterface<T> intrface) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtLiteral(CtLiteral<T> literal) {
		visit(literal);
	}

	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		visit(localVariable);
	}

	@Override
	public <T> void visitCtLocalVariableReference(CtLocalVariableReference<T> reference) {
		visit(reference);		
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
		visit(newArray);
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
		visit(assignment);
		
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
		visit(parameter);
		
	}

	@Override
	public <T> void visitCtParameterReference(CtParameterReference<T> reference) {
		visit(reference);
	}

	@Override
	public <R> void visitCtReturn(CtReturn<R> returnStatement) {
		visit(returnStatement);
	}

	@Override
	public <R> void visitCtStatementList(CtStatementList statements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S> void visitCtSwitch(CtSwitch<S> switchStatement) {
		visit(switchStatement);
		
	}

	@Override
	public <T, S> void visitCtSwitchExpression(CtSwitchExpression<T, S> switchExpression) {
		visit(switchExpression);		
	}

	@Override
	public void visitCtSynchronized(CtSynchronized synchro) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCtThrow(CtThrow throwStatement) {
		visit(throwStatement);		
	}

	@Override
	public void visitCtTry(CtTry tryBlock) {
		visit(tryBlock);
	}

	@Override
	public void visitCtTryWithResource(CtTryWithResource tryWithResource) {
		visit(tryWithResource);
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
		visit(reference);
	}

	@Override
	public <T> void visitCtTypeAccess(CtTypeAccess<T> typeAccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
		visit(operator);
	}

	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		visit(variableRead);
	}

	@Override
	public <T> void visitCtVariableWrite(CtVariableWrite<T> variableWrite) {
		visit(variableWrite);
		
	}

	@Override
	public void visitCtWhile(CtWhile whileLoop) {
		visit(whileLoop);
	}

	@Override
	public <T> void visitCtAnnotationFieldAccess(CtAnnotationFieldAccess<T> annotationFieldAccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
		visit(fieldRead);
	}

	@Override
	public <T> void visitCtFieldWrite(CtFieldWrite<T> fieldWrite) {
		visit(fieldWrite);
	}

	@Override
	public <T> void visitCtSuperAccess(CtSuperAccess<T> f) {
		visit(f);
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
