package matcher.handlers;

import java.io.File;
import java.util.Iterator;
import java.util.List;
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
import matcher.processors.MoveActionsProcessor;
import matcher.processors.UpdateActionsProcessor;
import matcher.processors.VisibilityDeleteActionsProcessor;
import matcher.processors.VisibilityInsertActionsProcessor;
import matcher.processors.VisibilityUpdateActionsProcessor;
import matcher.utils.SpoonUtils;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;

public class DeltaInstanceHandler {
	
	public DeltaInstance getDeltaInstance(File base, File variant, ConflictPattern cp) 
			throws ApplicationException {

		if(base != null) {
			Diff diff = calculateDiff(base, variant);
			DeltaInstance deltaInstance = new DeltaInstance();
			processOperations(diff.getAllOperations(), deltaInstance, cp);
			return deltaInstance;
		}
		else {
			DeltaInstance deltaInstance = new DeltaInstance();
			processClassInsertion(variant, deltaInstance, cp);
			return deltaInstance;
		}
	}
	
	private void processClassInsertion(File variant, DeltaInstance deltaInstance, 
			ConflictPattern cp) throws ApplicationException {
		
		SpoonResource resource = SpoonUtils.getSpoonResource(variant);

		CtType<?> changedType = SpoonUtils.getCtType(resource);
		if(changedType.isClass()) {
			Launcher launcher = SpoonUtils.loadLauncher(resource);
			CtType<?> fullType = SpoonUtils.getFullChangedCtType(launcher, 
					changedType.getQualifiedName());
			processNewClassInsertedElements(fullType, deltaInstance, cp);
		}
	}
	
	private void processNewClassInsertedElements(CtType<?> type, DeltaInstance deltaInstance,
			ConflictPattern cp) {
		Iterator<CtElement> iterator = type.descendantIterator();
		while(iterator.hasNext()) {
			CtElement element = iterator.next();
			if(InsertActionsProcessor.ofInsertInterest(element)) {
				processInsert(element, deltaInstance, cp);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void processOperations(List<Operation> operations, 
			DeltaInstance deltaInstance, ConflictPattern cp) {
		for(Operation<?> o: operations) {
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
			else {
				processMoveAction(o, deltaInstance, cp);
			}
		}
	}

	private Diff calculateDiff(File base, File variant) throws ApplicationException {
		Launcher baseLauncher = null, varLauncher = null;
		SpoonResource baseResource = null, varResource = null;
		baseResource = SpoonUtils.getSpoonResource(base);
		varResource = SpoonUtils.getSpoonResource(variant);

		CtType<?> baseType = SpoonUtils.getCtType(baseResource);
		CtType<?> changedType = SpoonUtils.getCtType(varResource);
		
		if(baseType != null && baseType.isClass() 
				&& changedType != null &&changedType.isClass()) {
			baseLauncher = SpoonUtils.loadLauncher(baseResource);
			varLauncher = SpoonUtils.loadLauncher(varResource);
			return diff(SpoonUtils.getFullChangedCtType(baseLauncher, baseType.getQualifiedName())
					, SpoonUtils.getFullChangedCtType(varLauncher, changedType.getQualifiedName()));
		}
		return null;
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
	
	private void processMoveAction(Operation<?> o, DeltaInstance deltaInstance, ConflictPattern cp) {
		MoveActionsProcessor processor = new MoveActionsProcessor(cp);
		o.getSrcNode().accept(processor);
		Optional<ActionInstance> a = processor.getResult();
		if(a.isPresent())
			deltaInstance.addActionInstance(a.get());
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
		UpdateActionsProcessor processor = new UpdateActionsProcessor(cp, o.getDstNode());
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
		processInsert(o.getSrcNode(), deltaInstance, cp);
	}
	
	private void processInsert(CtElement e, DeltaInstance deltaInstance, ConflictPattern cp) {
		InsertActionsProcessor processor = new InsertActionsProcessor(cp);
		e.accept(processor);
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
