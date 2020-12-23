package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;

public class ClassVariableIdentifier extends AbstractVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getClassVariableIds();
		List<String> cs = changeInstance.getClassQualifiedNames();
		
		return combine(vars, cs);
	}

}
