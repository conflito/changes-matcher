package matcher.patterns.deltas;

import java.util.ArrayList;
import java.util.List;

import matcher.entities.Visibility;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.deltas.DeleteMethodAction;
import matcher.entities.deltas.Holder;
import matcher.patterns.FreeVariable;

public class DeleteMethodPatternAction extends DeletePatternAction {

	private Visibility visibility;
	
	private List<FreeVariable> compatibles;

	public DeleteMethodPatternAction(FreeVariable deletedEntity, FreeVariable holderEntity, Visibility visibility) {
		super(deletedEntity, holderEntity);
		this.visibility = visibility;
		compatibles = new ArrayList<>();
	}
	
	public void addCompatible(FreeVariable var) {
		compatibles.add(var);
	}
	
	@Override
	public boolean matches(ActionInstance action) {
		return action instanceof DeleteMethodAction && filled() && matches((DeleteMethodAction)action);
	}
	
	private boolean matches(DeleteMethodAction action) {
		return getAction() == action.getAction() &&
			   getDeletedEntity().matches(action.getDeletedEntity().getQualifiedName()) &&
			   getHolderEntity().matches(action.getHolderEntity().getQualifiedName()) &&
			   (visibility == null || visibility == action.getVisibility()) &&
			   compatiblesMatch(action);
	}
	
	private boolean compatiblesMatch(DeleteMethodAction action) {
		return compatibles.stream().allMatch(v -> matchesOne(v, action.getCompatibles()));
	}

	private boolean matchesOne(FreeVariable v, List<Holder> compatibles) {
		return compatibles.stream().anyMatch(h -> v.matches(h.getQualifiedName()));
	}
	
}
