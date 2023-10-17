package org.conflito.matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;

import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.patterns.ConflictPattern;

public interface IVariableIdentifier {
	
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance
			, ConflictPattern conflictPattern);
}
