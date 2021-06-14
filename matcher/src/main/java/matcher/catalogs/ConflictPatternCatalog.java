package matcher.catalogs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matcher.exceptions.ApplicationException;

import matcher.patterns.ConflictPattern;

public class ConflictPatternCatalog {
	
	private static final String CONFLICTS_DIR = "src" + File.separator + "main" + 
			File.separator + "resources" + File.separator + "conflict-patterns" + 
			File.separator;//-no-dependency
	
	private Map<String, ConflictPattern> patterns;
	
	public ConflictPatternCatalog() throws ApplicationException {
		patterns = new HashMap<>();
		loadPatterns();
	}
	
	public boolean hasPattern(String patternName) {
		return patterns.containsKey(patternName);
	}
	
	public ConflictPattern getPattern(String patternName) {
		return patterns.get(patternName);
	}
	
	public Iterable<ConflictPattern> getPatterns(){
		List<ConflictPattern> result = new ArrayList<>();
		for(ConflictPattern cp: patterns.values()) {
			if(cp.getConflictName().startsWith("Parallel"))
				result.add(0, cp);
			else
				result.add(cp);
		}
		return result;
	}
	
	private void loadPatterns() throws ApplicationException {
		File conflictsDir = new File(CONFLICTS_DIR);
		
		if(!conflictsDir.exists())
			throw new ApplicationException("Missing directory with conflict patterns");
		
		for(File f: conflictsDir.listFiles()) {
			String filePath = f.getPath();
			ConflictPattern cp = PatternParser.getConflictPattern(filePath);
			patterns.put(cp.getConflictName(), cp);
		}
	}
}
