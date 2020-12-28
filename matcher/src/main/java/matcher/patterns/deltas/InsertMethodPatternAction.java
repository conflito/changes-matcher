package matcher.patterns.deltas;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.Holder;
import matcher.entities.deltas.InsertMethodAction;
import matcher.patterns.FreeVariable;

public class InsertMethodPatternAction extends InsertPatternAction {

	private Visibility visibility;
	
	private List<FreeVariable> compatibles;

	public InsertMethodPatternAction(FreeVariable insertedEntity, FreeVariable holderEntity, 
			Visibility visibility) {
		super(insertedEntity, holderEntity);
		this.visibility = visibility;
		compatibles = new ArrayList<>();
	}
	
	public void addCompatible(FreeVariable var) {
		compatibles.add(var);
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof InsertMethodAction && filled() && matches((InsertMethodAction)action);
	}
	
	private boolean matches(InsertMethodAction action) {
		return getAction() == action.getAction() &&
			   getInsertedEntity().matches(action.getInsertedEntityQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntityQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility()) &&
			   compatiblesMatch(action);
	}
	
	private boolean compatiblesMatch(InsertMethodAction action) {
		return compatibles.stream().allMatch(v -> matchesOne(v, action.getCompatibles()));
	}

	private boolean matchesOne(FreeVariable v, List<Holder> compatibles) {
		return compatibles.stream().anyMatch(h -> v.matches(h.getQualifiedName()));
	}
	
	
}
