package org.conflito.matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.MapUtilities;

public class FieldTypeVariableIdentifier implements IVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result = fieldTypesInBase(changeInstance, conflictPattern);
		MapUtilities.mergeMaps(result, fieldTypesInDeltas(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> fieldTypesInBase(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getFieldTypesVariableIds();
		List<String> ts = changeInstance.getFieldTypesQualifiedNames();
		
		return MapUtilities.combine(vars, ts);
	}
	
	private Map<Integer, List<String>> fieldTypesInDeltas(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getDeltaFieldTypesVariableIds();
		List<String> ts = changeInstance.getDeltaFieldTypesQualifiedNames();
		return MapUtilities.combine(vars, ts);
	}

}
