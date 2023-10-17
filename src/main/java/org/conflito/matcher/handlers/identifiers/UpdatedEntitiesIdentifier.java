package org.conflito.matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.MapUtilities;

public class UpdatedEntitiesIdentifier implements IVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result = getUpdatedEntities(changeInstance, conflictPattern);
		MapUtilities.mergeMaps(result, getUpdatedFields(changeInstance, conflictPattern));
		MapUtilities.mergeMaps(result, getUpdatedInvocations(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> getUpdatedEntities(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getUpdatedVariableIds();
		List<String> entities = changeInstance.getUpdatedQualifiedNames();
		return MapUtilities.combine(vars, entities);
	}
	
	private Map<Integer, List<String>> getUpdatedFields(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getUpdatedFieldsVariableIds();
		List<String> entities = changeInstance.getUpdatedFieldsQualifiedNames();
		return MapUtilities.combine(vars, entities);
	}
	
	private Map<Integer, List<String>> getUpdatedInvocations(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getUpdatedInvocationsVariableIds();
		List<String> entities = changeInstance.getUpdatedInvocationsQualifiedNames();
		return MapUtilities.combine(vars, entities);
	}

}
