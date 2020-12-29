package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.utils.MapUtilities;
import matcher.patterns.ConflictPattern;

public class FieldVariableIdentifier implements IVariableIdentifier{
	
	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result = fieldsInBase(changeInstance, conflictPattern);
		MapUtilities.mergeMaps(result, fieldsInDeltas(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> fieldsInBase(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getFieldsVariableIds();
		List<String> fields = changeInstance.getFieldsQualifiedNames();
		Map<Integer, List<String>> result = MapUtilities.combine(vars, fields);
		List<Integer> accessVars = conflictPattern.getFieldAccessVariableIds();
		List<String> accessFields = changeInstance.getFieldsAccessQualifiedNames();
		MapUtilities.mergeMaps(result, MapUtilities.combine(accessVars, accessFields));
		return result;
	}
	
	private Map<Integer, List<String>> fieldsInDeltas(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){		
		List<Integer> vars = conflictPattern.getDeltaFieldsVariableIds();
		List<String> fields = changeInstance.getDeltaFieldsQualifiedNames();
		Map<Integer, List<String>> result = MapUtilities.combine(vars, fields);
		List<Integer> accessVars = conflictPattern.getDeltaFieldAccessVariableIds();
		List<String> accessFields = changeInstance.getDeltaFieldsAccessQualifiedNames();
		MapUtilities.mergeMaps(result, MapUtilities.combine(accessVars, accessFields));
		return result;
	}

}
