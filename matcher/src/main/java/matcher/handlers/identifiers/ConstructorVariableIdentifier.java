package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;

public class ConstructorVariableIdentifier extends AbstractVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result =  
				constructorsInBase(changeInstance, conflictPattern);
		mergeMaps(result, constructorsInDeltas(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> constructorsInBase(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getConstructorsVariableIds();
		List<String> cs = changeInstance.getConstructorsQualifiedNames();
		
		return combine(vars, cs);
	}
	
	private Map<Integer, List<String>> constructorsInDeltas(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getDeltaConstructorsVariableIds();
		List<String> cs = changeInstance.getDeltaConstructorsQualifiedNames();
		return combine(vars, cs);
	}

}
