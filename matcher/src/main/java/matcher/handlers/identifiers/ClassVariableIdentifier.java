package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;
import matcher.utils.MapUtilities;

public class ClassVariableIdentifier implements IVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getClassVariableIds();
		List<String> cs = changeInstance.getClassQualifiedNames();
		
		return MapUtilities.combine(vars, cs);
	}

}
