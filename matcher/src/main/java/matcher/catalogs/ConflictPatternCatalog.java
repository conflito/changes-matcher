package matcher.catalogs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import matcher.exceptions.ApplicationException;

import matcher.patterns.ConflictPattern;

public class ConflictPatternCatalog {
	
	private static final String CONFLICTS_DIR = "src" + File.separator + "main" + 
			File.separator + "resources" + File.separator + "conflict-patterns" + 
			File.separator;
	
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
		return patterns.values();
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
