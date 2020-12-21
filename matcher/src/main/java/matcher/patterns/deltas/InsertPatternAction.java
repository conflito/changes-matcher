package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.InsertAction;
import matcher.patterns.FreeVariable;

public class InsertPatternAction extends ActionPattern {

	private FreeVariable insertedEntity;
	private FreeVariable holderEntity;

	public InsertPatternAction(FreeVariable insertedEntity, FreeVariable holderEntity) {
		super(Action.INSERT);
		this.insertedEntity = insertedEntity;
		this.holderEntity = holderEntity;
	}
	
	public FreeVariable getInsertedEntity() {
		return insertedEntity;
	}

	public FreeVariable getHolderEntity() {
		return holderEntity;
	}

	@Override
	public boolean filled() {
		return insertedEntity.hasValue() && holderEntity.hasValue();
	}

	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertAction && filled() && matches((InsertAction)action);
	}
	
	private boolean matches(InsertAction action) {
		return getAction() == action.getAction() &&
			   insertedEntity.matches(action.getInsertedEntity().getQualifiedName()) &&
			   holderEntity.matches(action.getHolderEntity().getQualifiedName());
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(insertedEntity.isId(id))
			insertedEntity.setValue(value);
		else if(holderEntity.isId(id))
			holderEntity.setValue(value);
	}

	@Override
	public String toStringDebug() {
		return "insert #" + insertedEntity.getId() + " into #" + holderEntity.getId();
	}

}
