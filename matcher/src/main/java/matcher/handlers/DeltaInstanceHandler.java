package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import gumtree.spoon.AstComparator;
import gumtree.spoon.builder.CtVirtualElement;
import gumtree.spoon.builder.CtWrapper;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.processors.DeleteActionsProcessor;
import matcher.processors.InsertActionsProcessor;
import matcher.processors.UpdateActionsProcessor;
import matcher.processors.VisibilityDeleteActionsProcessor;
import matcher.processors.VisibilityInsertActionsProcessor;
import matcher.processors.VisibilityUpdateActionsProcessor;
import matcher.utils.SpoonLauncherUtils;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;

public class DeltaInstanceHandler {
	
	public DeltaInstance getDeltaInstance(File base, File variant, ConflictPattern cp) 
			throws ApplicationException {

		Diff diff = calculateDiff(base, variant);
		DeltaInstance deltaInstance = new DeltaInstance();
		for(Operation<?> o: diff.getAllOperations()) {
			if(isInsert(o)) {
				if(isVisibilityAction(o)) {
					processInsertVisibilityAction(o, deltaInstance, cp);
				}
				else {
					processInsertAction(o, deltaInstance, cp);
				}	
			}
			else if(isDelete(o)) {
				if(isVisibilityAction(o)) {
					processDeleteVisibilityAction(o, deltaInstance, cp);
				}
				else {
					processDeleteAction(o, deltaInstance, cp);
				}
			}
			else if(isUpdate(o)) {
				if(isVisibilityAction(o)) {
					processUpdateVisibilityAction(o, deltaInstance, cp);
				}
				else {
					processUpdateAction(o, deltaInstance, cp);
				}
			}
		}
		return deltaInstance;
	}
	
	private Diff calculateDiff(File base, File variant) throws ApplicationException {
		Launcher baseLauncher = new Launcher(), varLauncher = new Launcher();
		SpoonResource baseResource = null, varResource = null;
		try {
			baseResource = SpoonResourceHelper.createFile(base);
			varResource = SpoonResourceHelper.createFile(variant);
			baseLauncher.addInputResource(baseResource);
			varLauncher.addInputResource(varResource);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid specified file", e);
		}
		CtType<?> baseType = new AstComparator().getCtType(baseResource);
		CtType<?> changedType = new AstComparator().getCtType(varResource);
		if(baseType.isClass() && changedType.isClass()) {
			CtClass<?>baseClass = (CtClass<?>) new AstComparator().getCtType(baseResource);
			CtClass<?> changedClass = (CtClass<?>) new AstComparator().getCtType(varResource);
			SpoonLauncherUtils.loadClassTree(baseClass, baseLauncher);
			SpoonLauncherUtils.loadInvokedClasses(changedClass, varLauncher);
			SpoonLauncherUtils.loadClassTree(changedClass, varLauncher);
			SpoonLauncherUtils.loadInvokedClasses(changedClass, varLauncher);
		}
		return diff(SpoonLauncherUtils.getFullChangedCtType(baseLauncher, baseType)
				, SpoonLauncherUtils.getFullChangedCtType(varLauncher, changedType));
	}
	
	private Diff diff(CtType<?> first, CtType<?> second) throws ApplicationException {
		Diff diff = null;
		try {
			diff = new AstComparator().compare(first, second);
		} catch (Exception e) {
			throw new ApplicationException("Error calculating the delta", e);
		}
		return diff;
	}
	
	private void processDeleteVisibilityAction(Operation<?> o, DeltaInstance deltaInstance, 
			ConflictPattern cp) {
		VisibilityDeleteActionsProcessor processor = new VisibilityDeleteActionsProcessor(cp);
		processor.visit(o.getSrcNode());
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
	}

	private void processInsertVisibilityAction(Operation<?> o, DeltaInstance deltaInstance, 
			ConflictPattern cp) {
		VisibilityInsertActionsProcessor processor = new VisibilityInsertActionsProcessor(cp);
		processor.visit(o.getSrcNode());
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
	}

	private void processUpdateVisibilityAction(Operation<?> o, DeltaInstance deltaInstance, 
			ConflictPattern cp) {
		VisibilityUpdateActionsProcessor processor = new VisibilityUpdateActionsProcessor(cp);
		processor.visit(o.getSrcNode(), o.getDstNode());
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
	}

	private void processUpdateAction(Operation<?> o, DeltaInstance deltaInstance, ConflictPattern cp) {
		UpdateActionsProcessor processor = new UpdateActionsProcessor(cp);
		o.getSrcNode().accept(processor);
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
	}

	private boolean isUpdate(Operation<?> o) {
		return o.getAction().getName().equals("UPD");
	}

	private void processDeleteAction(Operation<?> o, DeltaInstance deltaInstance, ConflictPattern cp) {
		DeleteActionsProcessor processor = new DeleteActionsProcessor(cp);
		o.getSrcNode().accept(processor);
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
	}

	private boolean isDelete(Operation<?> o) {
		return o.getAction().getName().equals("DEL");
	}

	private void processInsertAction(Operation<?> o, DeltaInstance deltaInstance, ConflictPattern cp) {
		InsertActionsProcessor processor = new InsertActionsProcessor(cp);
		o.getSrcNode().accept(processor);
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
	}

	private boolean isInsert(Operation<?> o) {
		return o.getAction().getName().equals("INS");
	}
	
	private boolean isVisibilityAction(Operation<?> o) {
		return o.getSrcNode() instanceof CtWrapper && !(o.getSrcNode() instanceof CtVirtualElement);
	}
}
