package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;
import matcher.utils.MapUtilities;

public class UpdatedEntitiesIdentifier implements IVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getUpdatedVariableIds();
		List<String> entities = changeInstance.getUpdatedQualifiedNames();
		return MapUtilities.combine(vars, entities);
	}

}
