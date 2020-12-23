package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;

public class MethodVariableIdentifier extends AbstractVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result =  methodsInBase(changeInstance, conflictPattern);
		mergeMaps(result, methodsInDeltas(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> methodsInBase(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getMethodVariableIds();
		List<String> methods = changeInstance.getMethodsQualifiedNames();
		return combine(vars, methods);
	}
	
	private Map<Integer, List<String>> methodsInDeltas(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getDeltaMethodsVariableIds();
		List<String> methods = changeInstance.getDeltaMethodsQualifiedNames();
		return combine(vars, methods);
	}

}
