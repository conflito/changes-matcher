package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.patterns.FreeVariable;

public abstract class InsertPatternAction extends ActionPattern {

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
	
	public int getInsertedEntityId() {
		return insertedEntity.getId();
	}

	public FreeVariable getHolderEntity() {
		return holderEntity;
	}

	@Override
	public boolean filled() {
		return insertedEntity.hasValue() && (holderEntity == null || holderEntity.hasValue());
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(insertedEntity.isId(id))
			insertedEntity.setValue(value);
		else if(holderEntity != null && holderEntity.isId(id))
			holderEntity.setValue(value);
	}

	@Override
	public String toStringDebug() {
		return "insert #" + insertedEntity.getId() + " into #" + holderEntity.getId();
	}
	
	@Override
	public String toStringFilled() {
		return "insert #" + insertedEntity.getValue() + " into #" + holderEntity.getValue();
	}

	@Override
	public void clean() {
		insertedEntity.clean();
		if(holderEntity != null)
			holderEntity.clean();
	}

}
