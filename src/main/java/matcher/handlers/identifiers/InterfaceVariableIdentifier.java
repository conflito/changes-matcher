package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;
import matcher.utils.MapUtilities;

public class InterfaceVariableIdentifier implements IVariableIdentifier {

	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		List<Integer> vars = conflictPattern.getInterfaceVariableIds();
		List<String> is = changeInstance.getInterfacesQualifiedNames();
		
		return MapUtilities.combine(vars, is);
	}

}
