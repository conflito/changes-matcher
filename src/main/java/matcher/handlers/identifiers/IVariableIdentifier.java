package matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.patterns.ConflictPattern;

public interface IVariableIdentifier {
	
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance
			, ConflictPattern conflictPattern);
}
