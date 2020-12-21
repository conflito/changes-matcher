package matcher.handlers.identifiers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;

public class ConstructorVariableIdentifier implements VariableValueIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result =  new HashMap<>();
		
		List<Integer> vars = conflictPattern.getConstructorsVariableIds();
		List<String> cs = changeInstance.getConstructorsQualifiedNames();
		
		for(int i: vars) {
			result.put(i, cs);
		}
		
		return result;
	}

}
