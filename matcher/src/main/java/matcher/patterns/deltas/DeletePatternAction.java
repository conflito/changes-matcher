package matcher.patterns.deltas;

import matcher.entities.deltas.Action;
import matcher.patterns.FreeVariable;

public abstract class DeletePatternAction extends ActionPattern{

	private FreeVariable deletedEntity;
	private FreeVariable holderEntity;

	public DeletePatternAction(FreeVariable deletedEntity, FreeVariable holderEntity) {
		super(Action.DELETE);
		this.deletedEntity = deletedEntity;
		this.holderEntity = holderEntity;
	}
	
	public FreeVariable getDeletedEntity() {
		return deletedEntity;
	}
	
	public int getDeletedEntityId() {
		return deletedEntity.getId();
	}

	public FreeVariable getHolderEntity() {
		return holderEntity;
	}

	@Override
	public boolean filled() {
		return deletedEntity.hasValue() && holderEntity.hasValue();
	}

	@Override
	public void setVariableValue(int id, String value) {
		if(deletedEntity.isId(id))
			deletedEntity.setValue(value);
		else if(holderEntity.isId(id))
			holderEntity.setValue(value);
	}

	@Override
	public String toStringDebug() {
		return "remove #" + deletedEntity.getId() + " from #" + holderEntity.getId();
	}
	
	@Override
	public String toStringFilled() {
		return "remove #" + deletedEntity.getValue() + " from #" + holderEntity.getValue();
	}

	@Override
	public void clean() {
		deletedEntity.clean();
		holderEntity.clean();
	}

}
