package matcher.handlers.identifiers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;

public class ClassVariableIdentifier implements VariableValueIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result =  new HashMap<>();
		
		List<Integer> vars = conflictPattern.getClassVariableIds();
		List<String> cs = changeInstance.getClassQualifiedNames();
		
		for(int i: vars) {
			result.put(i, cs);
		}
		
		return result;
	}

}
