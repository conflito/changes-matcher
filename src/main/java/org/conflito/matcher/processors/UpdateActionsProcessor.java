package org.conflito.matcher.processors;

import java.lang.annotation.Annotation;
import java.util.Optional;
import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.FieldInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.UpdateConstructorAction;
import org.conflito.matcher.entities.deltas.UpdateDependencyAction;
import org.conflito.matcher.entities.deltas.UpdateFieldAction;
import org.conflito.matcher.entities.deltas.UpdateFieldTypeAction;
import org.conflito.matcher.entities.deltas.UpdateMethodAction;
import org.conflito.matcher.handlers.SpoonHandler;
import org.conflito.matcher.patterns.ConflictPattern;
import spoon.reflect.code.CtAnnotationFieldAccess;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssert;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtBreak;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtContinue;
import spoon.reflect.code.CtDo;
import spoon.reflect.code.CtExecutableReferenceExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtJavaDoc;
import spoon.reflect.code.CtJavaDocTag;
import spoon.reflect.code.CtLambda;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.code.CtOperatorAssignment;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtSuperAccess;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtSwitchExpression;
import spoon.reflect.code.CtSynchronized;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.code.CtThrow;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtTryWithResource;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.code.CtWhile;
import spoon.reflect.code.CtYieldStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationMethod;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtCompilationUnit;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtEnum;
import spoon.reflect.declaration.CtEnumValue;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtImport;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModule;
import spoon.reflect.declaration.CtModuleRequirement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtPackageDeclaration;
import spoon.reflect.declaration.CtPackageExport;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtProvidedService;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.declaration.CtUsedService;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtCatchVariableReference;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtIntersectionTypeReference;
import spoon.reflect.reference.CtLocalVariableReference;
import spoon.reflect.reference.CtModuleReference;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtParameterReference;
import spoon.reflect.reference.CtTypeMemberWildcardImportReference;
import spoon.reflect.reference.CtTypeParameterReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtUnboundVariableReference;
import spoon.reflect.reference.CtWildcardReference;
import spoon.reflect.visitor.CtVisitor;
import spoon.support.reflect.declaration.CtClassImpl;

public class UpdateActionsProcessor extends DeltaProcessor implements CtVisitor {

  private final CtElement newOne;

  public UpdateActionsProcessor(ConflictPattern conflictPattern, CtElement newOne) {
    super(conflictPattern);
    this.newOne = newOne;
  }

  @Override
  public <T> void visitCtMethod(CtMethod<T> method) {
    if (getConflictPattern().hasUpdateActions()) {
      if (method.getDeclaringType() instanceof CtClass ||
          method.getDeclaringType() instanceof CtClassImpl) {
        ClassInstance classInstance = getClassInstance(method);
        MethodInstance methodInstance = getMethodInstance(method);
        ActionInstance result =
            new UpdateMethodAction(methodInstance, classInstance);
        setResult(result);
      }
    }
  }

  @Override
  public <T> void visitCtConstructor(CtConstructor<T> c) {
    if (getConflictPattern().hasUpdateActions()) {
      if (c.getDeclaringType() instanceof CtClass ||
          c.getDeclaringType() instanceof CtClassImpl) {
        ClassInstance holderInstance = getClassInstance(c);
        ConstructorInstance cInstance =
            getConstructorInstance(c, holderInstance);
        ActionInstance result = new UpdateConstructorAction(cInstance);
        setResult(result);
      }
    }
  }

  @Override
  public <T> void visitCtField(CtField<T> f) {
    if (getConflictPattern().hasUpdateFieldActions()) {
      if (f.getDeclaringType() instanceof CtClass ||
          f.getDeclaringType() instanceof CtClassImpl) {
        FieldInstance fieldInstance = getFieldInstance(f);
        ClassInstance classInstance = getClassInstance(f);
        ActionInstance result =
            new UpdateFieldAction(fieldInstance, classInstance);
        setResult(result);
      }
    } else if (getConflictPattern().hasUpdateFieldTypeActions()) {
      Optional<CtField<?>> newF = getFieldNode(newOne);
      if (newF.isPresent()) {
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
    if (getConflictPattern().hasUpdateInvocationActions()) {
      visitInvocationUpdate(invocation);
    } else {
      visit(invocation);
    }

  }

  private void visitInvocationUpdate(CtInvocation<?> invocation) {
    if (SpoonHandler.validInvocation(invocation) &&
        SpoonHandler.getMethodFromInvocation(invocation) != null) {
      CtMethod<?> oldInvocation = SpoonHandler.getMethodFromInvocation(invocation);
      CtMethod<?> newInvocation = SpoonHandler.getMethodFromInvocation((CtInvocation<?>) newOne);
      MethodInstance oldDependency = getMethodInstance(oldInvocation);
      MethodInstance newDependency = getMethodInstance(newInvocation);
      Optional<CtMethod<?>> possibleCaller = getMethodNode(invocation);
      if (possibleCaller.isPresent()) {
        Optional<CtMethod<?>> newPossibleCaller = getMethodNode(newOne);
        if (newPossibleCaller.isPresent()) {
          CtMethod<?> oldMethod = possibleCaller.get();
          CtMethod<?> newMethod = newPossibleCaller.get();
          MethodInstance oldMethodInstance = getMethodInstance(oldMethod);
          MethodInstance newMethodInstance = getMethodInstance(newMethod);
          ActionInstance result =
              new UpdateDependencyAction(oldMethodInstance, newMethodInstance,
                  oldDependency, newDependency);
          setResult(result);
        }

      } else {
        Optional<CtConstructor<?>> constructor = getConstructorNode(invocation);
        if (constructor.isPresent()) {
          Optional<CtConstructor<?>> newConstructor = getConstructorNode(invocation);
          if (newConstructor.isPresent()) {
            CtConstructor<?> c = constructor.get();
            CtConstructor<?> nC = newConstructor.get();
            if (c.getDeclaringType() instanceof CtClass ||
                c.getDeclaringType() instanceof CtClassImpl) {
              ClassInstance classInstance = getClassInstance(c);
              ConstructorInstance cInstance =
                  getConstructorInstance(c, classInstance);
              ConstructorInstance nCInstance =
                  getConstructorInstance(nC, classInstance);
              ActionInstance result =
                  new UpdateDependencyAction(cInstance, nCInstance,
                      oldDependency, newDependency);
              setResult(result);
            }
          }
        }
      }
    }

  }

  private void visit(CtElement element) {
    Optional<CtMethod<?>> method = getMethodNode(element);
    if (method.isPresent()) {
      method.get().accept(this);
    } else {
      Optional<CtConstructor<?>> c = getConstructorNode(element);
      if (c.isPresent()) {
        c.get().accept(this);
      } else {
        Optional<CtField<?>> f = getFieldNode(element);
        if (f.isPresent()) {
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
    visit(typeAccess);
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
  public void visitCtTypeMemberWildcardImportReference(
      CtTypeMemberWildcardImportReference wildcardReference) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visitCtYieldStatement(CtYieldStatement statement) {
    // TODO Auto-generated method stub

  }
}
