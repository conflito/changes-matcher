package matcher.handlers;

import java.io.File;
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

public class DeltaInstanceHandler {
	
	public DeltaInstance getDeltaInstance(File base, File variant, ConflictPattern cp) 
			throws ApplicationException {
		DeltaInstance deltaInstance = new DeltaInstance();
		Diff diff = null;
		try {
			diff = new AstComparator().compare(base, variant);
		} catch (Exception e) {
			throw new ApplicationException("Error calculating the delta", e);
		}
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
