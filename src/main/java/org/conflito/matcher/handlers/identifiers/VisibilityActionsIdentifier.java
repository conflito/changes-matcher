package org.conflito.matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.MapUtilities;

public class VisibilityActionsIdentifier implements IVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getVisibilityActionsVariableIds();
		List<String> entities = changeInstance.getVisibilityActionsQualifiedNames();
		return MapUtilities.combine(vars, entities);
	}

}
