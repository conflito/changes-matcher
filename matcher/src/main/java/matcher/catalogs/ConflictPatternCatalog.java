package matcher.catalogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import matcher.exceptions.ApplicationException;

import matcher.patterns.ConflictPattern;

public class ConflictPatternCatalog {
	
	private static final String CONFLICTS_DIR = "src" + File.separator + "main" + 
			File.separator + "resources" + File.separator + "conflict-patterns" + 
			File.separator;
	
	private List<ConflictPattern> patterns;
	
	public ConflictPatternCatalog() throws ApplicationException {
		patterns = new ArrayList<>();
		loadPatterns();
	}
	
	public Iterable<ConflictPattern> getPatterns(){
		return patterns;
	}
	
	private void loadPatterns() throws ApplicationException {
		File conflictsDir = new File(CONFLICTS_DIR);
		
		if(!conflictsDir.exists())
			throw new ApplicationException("Missing directory with conflict patterns");
		
		for(File f: conflictsDir.listFiles()) {
			String filePath = f.getPath();
			ConflictPattern cp = PatternParser.getConflictPattern(filePath);
			patterns.add(cp);
		}
	}
}
