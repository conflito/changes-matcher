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
	
	private SpoonUtils spoonHandler;
	
	public DeltaInstanceHandler(SpoonUtils spoonHandler) {
		this.spoonHandler = spoonHandler;
	}

	public DeltaInstance getDeltaInstance(File base, File variant, ConflictPattern cp) 
			throws ApplicationException {

		if(base != null) {
			Diff diff = calculateDiff(base, variant);
			DeltaInstance deltaInstance = new DeltaInstance();
			if(diff != null)
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
		
		SpoonResource resource = spoonHandler.getSpoonResource(variant);

		CtType<?> changedType = spoonHandler.getCtType(resource);
		if(changedType.isClass()) {
			Launcher launcher = spoonHandler.loadLauncher(resource);
			CtType<?> fullType = spoonHandler.getFullChangedCtType(launcher, 
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
				processInsertOperation(o, deltaInstance, cp);
			}
			else if(isDelete(o)) {
				processDeleteOperation(o, deltaInstance, cp);
			}
			else if(isUpdate(o)) {
				processUpdateOperation(o, deltaInstance, cp);
			}
			else {
				processMoveOperation(o, deltaInstance, cp);
			}
		}
	}
	
	private void processInsertOperation(Operation<?> o, DeltaInstance deltaInstance, 
			ConflictPattern cp) {
		if(isVisibilityAction(o)) {
			processInsertVisibilityAction(o, deltaInstance, cp);
		}
		else {
			processInsertAction(o, deltaInstance, cp);
		}	
	}
	
	private void processDeleteOperation(Operation<?> o, DeltaInstance deltaInstance, 
			ConflictPattern cp) {
		if(isVisibilityAction(o)) {
			processDeleteVisibilityAction(o, deltaInstance, cp);
		}
		else {
			processDeleteAction(o, deltaInstance, cp);
		}
	}

	private void processUpdateOperation(Operation<?> o, DeltaInstance deltaInstance, 
			ConflictPattern cp) {
		if(isVisibilityAction(o)) {
			processUpdateVisibilityAction(o, deltaInstance, cp);
		}
		else {
			processUpdateAction(o, deltaInstance, cp);
		}
	}
	
	private Diff calculateDiff(File base, File variant) throws ApplicationException {
		Launcher baseLauncher = null;
		Launcher varLauncher = null;
		SpoonResource baseResource = null;
		SpoonResource varResource = null;
		
		baseResource = spoonHandler.getSpoonResource(base);
		varResource = spoonHandler.getSpoonResource(variant);

		CtType<?> baseType = spoonHandler.getCtType(baseResource);
		CtType<?> changedType = spoonHandler.getCtType(varResource);
		
		if(baseType != null && baseType.isClass() 
				&& changedType != null &&changedType.isClass()) {
			baseLauncher = spoonHandler.loadLauncher(baseResource);
			varLauncher = spoonHandler.loadLauncher(varResource);
			return diff(spoonHandler.getFullChangedCtType(baseLauncher, baseType.getQualifiedName())
					, spoonHandler.getFullChangedCtType(varLauncher, changedType.getQualifiedName()));
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
	
	private void processMoveOperation(Operation<?> o, DeltaInstance deltaInstance, ConflictPattern cp) {
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
