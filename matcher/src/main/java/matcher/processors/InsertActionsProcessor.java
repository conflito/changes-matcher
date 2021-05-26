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
import matcher.entities.deltas.InsertClassAction;
import matcher.entities.deltas.InsertConstructorAction;
import matcher.entities.deltas.InsertFieldAccessAction;
import matcher.entities.deltas.InsertFieldAction;
import matcher.entities.deltas.InsertInvocationAction;
import matcher.entities.deltas.InsertMethodAction;
import matcher.entities.deltas.UpdateConstructorAction;
import matcher.entities.deltas.UpdateMethodAction;
import matcher.handlers.SpoonHandler;
import matcher.patterns.ConflictPattern;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.CtVisitor;

public class InsertActionsProcessor extends DeltaProcessor implements CtVisitor{
	
	public InsertActionsProcessor(ConflictPattern conflictPattern) {
		super(conflictPattern);
	}
	
	public static boolean ofInsertInterest(CtElement element) {
		return element instanceof CtMethod ||
			   element instanceof CtConstructor ||
			   element instanceof CtField ||
			   element instanceof CtClass ||
			   element instanceof CtInvocation ||
			   element instanceof CtFieldRead ||
			   element instanceof CtFieldWrite;
	}
	
	@Override
	public <T> void visitCtMethod(CtMethod<T> method) {
		if(getConflictPattern().hasInsertMethodActions()) {
			ClassInstance holderInstance = getClassInstance(method);
			MethodInstance insertedInstance = getMethodInstance(method);
			InsertMethodAction result = new InsertMethodAction(insertedInstance, holderInstance);
			boolean stop = false;
			do {
				for(MethodInstance m: holderInstance.getMethods()) {
					if(!insertedInstance.equals(m) && insertedInstance.isCompatibleWith(m))
						result.addCompatible(m);
				}
				if(holderInstance.getSuperClass().isPresent())
					holderInstance = holderInstance.getSuperClass().get();
				else
					stop = true;
			}while(!stop);
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtConstructor(CtConstructor<T> c) {
		if(getConflictPattern().hasInsertConstructorActions()) {
			ClassInstance holderInstance = getClassInstance(c);
			ConstructorInstance insertedInstance = getConstructorInstance(c, holderInstance);
			ActionInstance result = new InsertConstructorAction(insertedInstance, holderInstance);
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtField(CtField<T> field) {
		if(getConflictPattern().hasInsertFieldActions()) {
			ClassInstance holderInstance = getClassInstance(field);
			FieldInstance insertedInstance = getFieldInstance(field);
			ActionInstance result = new InsertFieldAction(insertedInstance, holderInstance);
			setResult(result);
		}
	}
	
	@Override
	public <T> void visitCtClass(CtClass<T> ctClass) {
		if(getConflictPattern().hasInsertClassActions()) {
			ClassInstance insertedInstance = getClassInstance(ctClass, true);
			ActionInstance result = new InsertClassAction(insertedInstance);
			setResult(result);
		}
		
	}
	
	@Override
	public <T> void visitCtInvocation(CtInvocation<T> invocation) {
		if(SpoonHandler.validInvocation(invocation) && 
				(SpoonHandler.invocationOfObjectMethod(invocation) ||
				SpoonHandler.invocationFromTheSystem(invocation))) {
			if(getConflictPattern().hasInsertInvocationActions()) {
				CtMethod<?> ctMethod = SpoonHandler.getMethodFromInvocation(invocation);
				MethodInstance invoked = getMethodInstance(ctMethod);
				MethodInvocationInstance mii = new MethodInvocationInstance(invoked);
				Optional<CtMethod<?>> possibleCaller = getMethodNode(invocation);
				if(possibleCaller.isPresent()) {
					CtMethod<?> method = possibleCaller.get();
					MethodInstance methodInstance = getMethodInstance(method);
					ActionInstance result = new InsertInvocationAction(mii, methodInstance);
					setResult(result);
				}
				else {
					Optional<CtConstructor<?>> constructor = getConstructorNode(invocation);
					if(constructor.isPresent()) {
						CtConstructor<?> c = constructor.get();
						ClassInstance classInstance = getClassInstance(c);
						ConstructorInstance insertedInstance = getConstructorInstance(c, classInstance);
						ActionInstance result = new InsertInvocationAction(mii, insertedInstance);
						setResult(result);
					}
				}
			}
			else {
				visit(invocation);
			}
		}
	}
	
	@Override
	public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
		if(getConflictPattern().hasInsertFieldAccessActions()) {
			String fieldQualifiedName = fieldRead.getVariable().getSimpleName();
			FieldAccessInstance fai = new FieldAccessInstance(fieldQualifiedName, 
					FieldAccessType.READ);
			Optional<CtMethod<?>> possibleCaller = getMethodNode(fieldRead);
			if(possibleCaller.isPresent()) {
				CtMethod<?> method = possibleCaller.get();
				MethodInstance methodInstance = getMethodInstance(method);
				ActionInstance result = new InsertFieldAccessAction(fai, methodInstance);
				setResult(result);
			}
		}
		else {
			visit(fieldRead);
		}
	}

	@Override
	public <T> void visitCtFieldWrite(CtFieldWrite<T> fieldWrite) {
		if(getConflictPattern().hasInsertFieldAccessActions()) {
			String fieldQualifiedName = fieldWrite.getVariable().getSimpleName();
			FieldAccessInstance fai = new FieldAccessInstance(fieldQualifiedName, 
					FieldAccessType.WRITE);
			Optional<CtMethod<?>> possibleCaller = getMethodNode(fieldWrite);
			if(possibleCaller.isPresent()) {
				CtMethod<?> method = possibleCaller.get();
				MethodInstance methodInstance = getMethodInstance(method);
				ActionInstance result = new InsertFieldAccessAction(fai, methodInstance);
				setResult(result);
			}
		}
		else {
			visit(fieldWrite);
		}
	}
	
	private void visit(CtElement element) {
		if(getConflictPattern().hasUpdateActions()) {
			Optional<CtMethod<?>> possibleCaller = getMethodNode(element);
			if(possibleCaller.isPresent()) {
				CtMethod<?> method = possibleCaller.get();
				MethodInstance methodInstance = getMethodInstance(method);
				ActionInstance result = new UpdateMethodAction(methodInstance);
				setResult(result);
			}
			else {
				Optional<CtConstructor<?>> c = getConstructorNode(element);
				if(c.isPresent()) {
					ClassInstance holderInstance = getClassInstance(c.get());
					ConstructorInstance cInstance = getConstructorInstance(c.get(), holderInstance);
					ActionInstance result = new UpdateConstructorAction(cInstance);
					setResult(result);
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
		visit(breakStatement);
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
	public void visitCtTypeParameter(CtTypeParameter typeParameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtConditional(CtConditional<T> conditional) {
		visit(conditional);
	}

	@Override
	public void visitCtContinue(CtContinue continueStatement) {
		visit(continueStatement);
	}

	@Override
	public void visitCtDo(CtDo doLoop) {
		visit(doLoop);
	}

	@Override
	public <T extends Enum<?>> void visitCtEnum(CtEnum<T> ctEnum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtExecutableReference(CtExecutableReference<T> reference) {
		visit(reference);
	}

	@Override
	public <T> void visitCtEnumValue(CtEnumValue<T> enumValue) {
		visit(enumValue);
	}

	@Override
	public <T> void visitCtThisAccess(CtThisAccess<T> thisAccess) {
		visit(thisAccess);
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
		visit(catchVariable);
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
		visit(ctConstructorCall);
	}

	@Override
	public <T> void visitCtNewClass(CtNewClass<T> newClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void visitCtLambda(CtLambda<T> lambda) {
		visit(lambda);
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
