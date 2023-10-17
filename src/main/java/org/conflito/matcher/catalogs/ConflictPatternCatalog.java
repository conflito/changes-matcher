package org.conflito.matcher.catalogs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.conflito.matcher.exceptions.ApplicationException;

import org.conflito.matcher.patterns.ConflictPattern;

public class ConflictPatternCatalog {
	
	private static final String CONFLICTS_DIR = "conflict-patterns";
	
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
	
	public List<ConflictPattern> getPatterns(){
		List<ConflictPattern> result = new ArrayList<>();
		for(ConflictPattern cp: patterns.values()) {
			if(cp.getConflictName().startsWith("Parallel"))
				result.add(0, cp);
			else
				result.add(cp);
		}
		return result;
	}
	
	public List<String> getPatternsNames(){
		return getPatterns().stream()
				.map(ConflictPattern::getConflictName)
				.collect(Collectors.toList());
	}
	
	private void loadPatterns() throws ApplicationException {
		for(String fileName: getConflictFileNames()) {
			addPattern(fileName);
		}
	}
	
	private void addPattern(String resourceName) throws ApplicationException {
		BufferedReader reader = getResource(resourceName);
		ConflictPattern cp = PatternParser.getConflictPattern(reader);
		patterns.put(cp.getConflictName(), cp);
	}
	
	private static List<String> getConflictFileNames() throws ApplicationException {
		List<String> result = new ArrayList<>();
		
		result.add(CONFLICTS_DIR + "/AddOverridingNewClass.co");
		result.add(CONFLICTS_DIR + "/AddOverridingNewDependency.co");
		result.add(CONFLICTS_DIR + "/AddOverridingNewMethod.co");
		result.add(CONFLICTS_DIR + "/ChangeMethod.co");
		result.add(CONFLICTS_DIR + "/ChangeMethod2.co");
		result.add(CONFLICTS_DIR + "/ChangeMethod3.co");
		result.add(CONFLICTS_DIR + "/DependencyBasedNewClass.co");
		result.add(CONFLICTS_DIR + "/DependencyBasedNewDependency.co");
		result.add(CONFLICTS_DIR + "/DependencyBasedNewMethod.co");
		result.add(CONFLICTS_DIR + "/FieldHiding.co");
		result.add(CONFLICTS_DIR + "/OverloadByAccessChange2NewClass.co");
		result.add(CONFLICTS_DIR + "/OverloadByAccessChange2NewDependency.co");
		result.add(CONFLICTS_DIR + "/OverloadByAccessChange2NewMethod.co");
		result.add(CONFLICTS_DIR + "/OverloadByAccessChangeNewClass.co");
		result.add(CONFLICTS_DIR + "/OverloadByAccessChangeNewDependency.co");
		result.add(CONFLICTS_DIR + "/OverloadByAccessChangeNewMethod.co");
		result.add(CONFLICTS_DIR + "/OverloadByAdditionNewClass.co");
		result.add(CONFLICTS_DIR + "/OverloadByAdditionNewDependency.co");
		result.add(CONFLICTS_DIR + "/OverloadByAdditionNewMethod.co");
		result.add(CONFLICTS_DIR + "/ParallelChanges.co");
		result.add(CONFLICTS_DIR + "/ParallelChangesConstructor.co");
		result.add(CONFLICTS_DIR + "/ParallelChangesField.co");
		result.add(CONFLICTS_DIR + "/RemoveOverriding.co");
		result.add(CONFLICTS_DIR + "/UnexpectedOverriding3NewClass.co");
		result.add(CONFLICTS_DIR + "/UnexpectedOverriding3NewDependency.co");
		result.add(CONFLICTS_DIR + "/UnexpectedOverriding3NewMethod.co");
		result.add(CONFLICTS_DIR + "/UnexpectedOverridingNewClass.co");
		result.add(CONFLICTS_DIR + "/UnexpectedOverridingNewDependency.co");
		result.add(CONFLICTS_DIR + "/UnexpectedOverridingNewMethod.co");
		
		return result;
	}
	
	private static BufferedReader getResource(String name) throws ApplicationException{
		try {
			InputStream is = getContextClassLoader()
					.getResourceAsStream(name);
			InputStreamReader streamReader =
					new InputStreamReader(is, StandardCharsets.UTF_8);
			return new BufferedReader(streamReader);
		}
		catch(Exception e) {
			throw new ApplicationException("Invalid resource name");
		}
	}
	
	private static ClassLoader getContextClassLoader() {
	    return Thread.currentThread().getContextClassLoader();
	}
}
