package org.conflito.matcher.processors;

import java.lang.annotation.Annotation;
import java.util.Optional;
import org.conflito.matcher.entities.ClassInstance;
import org.conflito.matcher.entities.ConstructorInstance;
import org.conflito.matcher.entities.FieldAccessInstance;
import org.conflito.matcher.entities.FieldAccessType;
import org.conflito.matcher.entities.FieldInstance;
import org.conflito.matcher.entities.MethodInstance;
import org.conflito.matcher.entities.MethodInvocationInstance;
import org.conflito.matcher.entities.deltas.ActionInstance;
import org.conflito.matcher.entities.deltas.DeleteConstructorAction;
import org.conflito.matcher.entities.deltas.DeleteFieldAccessAction;
import org.conflito.matcher.entities.deltas.DeleteFieldAction;
import org.conflito.matcher.entities.deltas.DeleteInvocationAction;
import org.conflito.matcher.entities.deltas.DeleteMethodAction;
import org.conflito.matcher.entities.deltas.UpdateConstructorAction;
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

public class DeleteActionsProcessor extends DeltaProcessor implements CtVisitor {

  public DeleteActionsProcessor(ConflictPattern conflictPattern) {
    super(conflictPattern);
  }

  @Override
  public <T> void visitCtConstructor(CtConstructor<T> c) {
    if (getConflictPattern().hasDeleteConstructorsActions()) {
      if (c.getDeclaringType() instanceof CtClass ||
          c.getDeclaringType() instanceof CtClassImpl) {
        ClassInstance holderInstance = getClassInstance(c);
        ConstructorInstance deletedInstance = getConstructorInstance(c, holderInstance);
        ActionInstance result = new DeleteConstructorAction(deletedInstance, holderInstance);
        setResult(result);
      }
    }

  }

  @Override
  public <T> void visitCtField(CtField<T> field) {
    if (getConflictPattern().hasDeleteFieldActions()) {
      if (field.getDeclaringType() instanceof CtClass ||
          field.getDeclaringType() instanceof CtClassImpl) {
        ClassInstance holderInstance = getClassInstance(field);
        FieldInstance deletedInstance = getFieldInstance(field);
        ActionInstance result = new DeleteFieldAction(deletedInstance, holderInstance);
        setResult(result);
      }
    }
  }

  @Override
  public <T> void visitCtInvocation(CtInvocation<T> invocation) {
    if (SpoonHandler.validInvocation(invocation) &&
        SpoonHandler.getMethodFromInvocation(invocation) != null &&
        (SpoonHandler.invocationOfObjectMethod(invocation) ||
            SpoonHandler.invocationFromTheSystem(invocation))) {
      if (getConflictPattern().hasDeleteInvocationActions()) {
        CtMethod<?> ctMethod = SpoonHandler.getMethodFromInvocation(invocation);
        MethodInstance invoked = getMethodInstance(ctMethod);
        MethodInvocationInstance mii = new MethodInvocationInstance(invoked);
        Optional<CtMethod<?>> possibleCaller = getMethodNode(invocation);
        if (possibleCaller.isPresent()) {
          CtMethod<?> method = possibleCaller.get();
          MethodInstance methodInstance = getMethodInstance(method);
          ActionInstance result = new DeleteInvocationAction(mii, methodInstance);
          setResult(result);
        } else {
          Optional<CtConstructor<?>> constructor = getConstructorNode(invocation);
          if (constructor.isPresent()) {
            CtConstructor<?> c = constructor.get();
            if (c.getDeclaringType() instanceof CtClass ||
                c.getDeclaringType() instanceof CtClassImpl) {
              ClassInstance classInstance = getClassInstance(c);
              ConstructorInstance constructorInstance =
                  getConstructorInstance(c, classInstance);
              ActionInstance result =
                  new DeleteInvocationAction(mii, constructorInstance);
              setResult(result);
            }

          }
        }
      } else {
        visit(invocation);
      }
    }

  }

  @Override
  public <T> void visitCtMethod(CtMethod<T> method) {
    if (getConflictPattern().hasDeleteMethodActions()) {
      if (method.getDeclaringType() instanceof CtClass ||
          method.getDeclaringType() instanceof CtClassImpl) {
        ClassInstance holderInstance = getClassInstance(method);
        MethodInstance deletedInstance = getMethodInstance(method);
        ActionInstance result = new DeleteMethodAction(deletedInstance, holderInstance);
        setResult(result);
      }
    }
  }

  @Override
  public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
    if (getConflictPattern().hasDeleteFieldAccessActions()) {
      String fieldQualifiedName = fieldRead.getVariable().getSimpleName();
      FieldAccessInstance fai = new FieldAccessInstance(fieldQualifiedName
          , FieldAccessType.READ);
      Optional<CtMethod<?>> possibleCaller = getMethodNode(fieldRead);
      if (possibleCaller.isPresent()) {
        CtMethod<?> method = possibleCaller.get();
        MethodInstance methodInstance = getMethodInstance(method);
        ActionInstance result = new DeleteFieldAccessAction(fai, methodInstance);
        setResult(result);
      }
    } else {
      visit(fieldRead);
    }
  }

  @Override
  public <T> void visitCtFieldWrite(CtFieldWrite<T> fieldWrite) {
    if (getConflictPattern().hasDeleteFieldAccessActions()) {
      String fieldQualifiedName = fieldWrite.getVariable().getSimpleName();
      FieldAccessInstance fai = new FieldAccessInstance(fieldQualifiedName,
          FieldAccessType.WRITE);
      Optional<CtMethod<?>> possibleCaller = getMethodNode(fieldWrite);
      if (possibleCaller.isPresent()) {
        CtMethod<?> method = possibleCaller.get();
        MethodInstance methodInstance = getMethodInstance(method);
        ActionInstance result = new DeleteFieldAccessAction(fai, methodInstance);
        setResult(result);
      }
    } else {
      visit(fieldWrite);
    }
  }

  private void visit(CtElement element) {
    if (getConflictPattern().hasUpdateActions()) {
      Optional<CtMethod<?>> possibleCaller = getMethodNode(element);
      if (possibleCaller.isPresent()) {
        CtMethod<?> method = possibleCaller.get();
        if (method.getDeclaringType() instanceof CtClass ||
            method.getDeclaringType() instanceof CtClassImpl) {
          ClassInstance classInstance = getClassInstance(method);
          MethodInstance methodInstance = getMethodInstance(method);
          ActionInstance result =
              new UpdateMethodAction(methodInstance, classInstance);
          setResult(result);
        }
      } else {
        Optional<CtConstructor<?>> c = getConstructorNode(element);
        if (c.isPresent()) {
          CtConstructor<?> constructor = c.get();
          if (constructor.getDeclaringType() instanceof CtClass ||
              constructor.getDeclaringType() instanceof CtClassImpl) {
            ClassInstance holderInstance = getClassInstance(constructor);
            ConstructorInstance cInstance =
                getConstructorInstance(constructor, holderInstance);
            ActionInstance result = new UpdateConstructorAction(cInstance);
            setResult(result);
          }
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
    // TODO Auto-generated method stub

  }

  @Override
  public <T> void visitCtClass(CtClass<T> ctClass) {
    visit(ctClass);
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
    // TODO Auto-generated method stub

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
    visit(reference);
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
    // TODO Auto-generated method stub

  }

  @Override
  public <T, E extends CtExpression<?>> void visitCtExecutableReferenceExpression(
      CtExecutableReferenceExpression<T, E> expression) {
    visit(expression);
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
    // TODO Auto-generated method stub

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
