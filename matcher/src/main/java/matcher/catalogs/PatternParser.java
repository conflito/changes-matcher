package matcher.catalogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import matcher.entities.Visibility;
import matcher.exceptions.ApplicationException;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertClassPatternAction;
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.UpdateMethodPatternAction;
import matcher.patterns.goals.TestingGoal;

public class PatternParser {
	
	private static final String VAR_PATTERN = "\\$[A-Z]([1-9]+)?";
	private static final Pattern VAR_REGEX_PATTERN = Pattern.compile(VAR_PATTERN);
		
	private Map<String, FreeVariable> variables;
	
	private Map<String, ClassPattern> definedClasses;
	private Map<String, MethodPattern> definedMethods;
	
	private Map<String, ClassPattern> insertedClasses;
	
	private int lineNumber;
	
	private ConflictPattern conflictPattern;
	private DeltaPattern currentDelta;
	
	public PatternParser() {
		lineNumber = 1;
		variables = new HashMap<>();
		definedClasses = new HashMap<>();
		definedMethods = new HashMap<>();
		
		insertedClasses = new HashMap<>();
		
		currentDelta = new DeltaPattern();
	}
	
	public static ConflictPattern getConflictPattern(String filePath) throws ApplicationException {
		return new PatternParser().getPattern(filePath);
	}
	
	private ConflictPattern getPattern(String filePath) throws ApplicationException {
		if(filePath == null)
			throw new ApplicationException("Path to conflict pattern can't be null");
		File f = new File(filePath);		
		
		BufferedReader br = openReader(f);
		
		String name = parseName(br);
		
		conflictPattern = new ConflictPattern(name);
		
		parseVariables(br);
		
		processAdditionalRules(br);
		
		processBasePattern(br);
		
		processDeltaPattern(br, true);
		
		processDeltaPattern(br, false);
		
		processTestingGoal(br);
		
		closeReader(br);
		
		return conflictPattern;
	}
	
	private int getCurrentLine() {
		return lineNumber - 1;
	}

	private boolean existsVariable(String variable) {
		return variables.containsKey(variable);
	}
	
	private boolean existsClass(String variable) {
		return definedClasses.containsKey(variable) ||
				insertedClasses.containsKey(variable);
	}
	
	private ClassPattern getClassPattern(String variable) {
		if(definedClasses.containsKey(variable))
			return definedClasses.get(variable);
		else
			return insertedClasses.get(variable);
	}
	
	private boolean existsMethod(String variable) {
		return definedMethods.containsKey(variable);
	}
	
	private BufferedReader openReader(File f) throws ApplicationException {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File for conflict pattern doesn't exist");
		}
		
		return br;
	}
	
	private void closeReader(Reader reader) throws ApplicationException {
		try {
			reader.close();
		} catch (IOException e) {
			throw new ApplicationException("Something went wrong reading the file");
		}
	}
	
	private String readLine(BufferedReader br) throws ApplicationException {
		String line;
		try {
			line = br.readLine();
		} catch (IOException e) {
			throw new ApplicationException("Something went wrong reading the file");
		}
		lineNumber++;
		return line;
	}
	
	private String parseName(BufferedReader br) throws ApplicationException {
		String line = readLine(br);
		String name = StringUtils.substringBetween(line, "\"");
		
		if(name == null)
			throw new ApplicationException("Invalid pattern: invalid pattern name");
		
		return name;
	}
	
	private void parseVariables(BufferedReader br) throws ApplicationException {
		String line = readLine(br);
		String components[] = line.split("\\s+");
		if(components.length <= 1)
			throw new ApplicationException("Invalid pattern: minimum number "
					+ "of variables is 1");
		
		for(int i = 1; i < components.length; i++) {
			String var = components[i];
			if(!var.matches(VAR_PATTERN))
				throw new ApplicationException("Invalid pattern: invalid variable "
						+ " definition " + var);
			
			if(existsVariable(var))
				throw new ApplicationException("Invalid pattern: duplicate variable "
						+ var);
			
			variables.put(var, new FreeVariable(i-1));
		}
	}
	
	private void processAdditionalRules(BufferedReader br) throws ApplicationException {
		boolean existRules = findAdditonalRules(br);
		if(existRules) {
			String line;
			try {
				do{
					line = readLine(br);
					
					if(!line.equals("")) {
						if(isDifferentVariableRule(line))
							processDifferentVariableRule(line);
						else if(isCanBeEqualVariableRule(line))
							processCanBeEqualVariableRule(line);
					}
					
				}while(!line.startsWith("Base Condition:"));
			}
			catch (NullPointerException e) {
				throw new ApplicationException("Missing base condition");	
			} 
		}
	}
	
	private void processDifferentVariableRule(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		
		if(matcher.find()) {
			String firstVar = matcher.group();
			
			if(!existsVariable(firstVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ firstVar  + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String secondVar = matcher.group();
				
				if(!existsVariable(secondVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ secondVar  + " in line " + getCurrentLine());
				
				conflictPattern.addDifferentVariablesRule(variables.get(firstVar), 
						variables.get(secondVar));
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processCanBeEqualVariableRule(String line) 
			throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		
		if(matcher.find()) {
			String firstVar = matcher.group();
			
			if(!existsVariable(firstVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ firstVar  + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String secondVar = matcher.group();
				
				if(!existsVariable(secondVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ secondVar  + " in line " + getCurrentLine());
				
				conflictPattern.addEqualVariableRule(variables.get(firstVar), 
						variables.get(secondVar));
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private boolean findAdditonalRules(BufferedReader br) throws ApplicationException {
		String line;
		try {
			do{
				line = readLine(br);
			}while(!line.startsWith("Additional Rules:") &&
					!line.startsWith("Base Condition:"));
		}
		catch (NullPointerException e) {
			throw new ApplicationException("Missing base condition");	
		} 
		return line.startsWith("Additional Rules:");
	}
	
	private void processBasePattern(BufferedReader br) throws ApplicationException {
		String line;
		do{
			line = readLine(br);
			processBaseConditionLine(line);
		} while(!line.startsWith("Delta:"));
		
		buildBasePattern();
	}
	
	private void buildBasePattern() {
		BasePattern basePattern = new BasePattern();
		for(Map.Entry<String, ClassPattern> e: definedClasses.entrySet()) {
			basePattern.addClassPattern(e.getValue());
		}
		conflictPattern.setBasePattern(basePattern);
	}

	private void processDeltaPattern(BufferedReader br, boolean firstDelta) throws ApplicationException {
		String line;
		do {
			line= readLine(br);
			processDeltaLine(line, firstDelta);
		} while((firstDelta && !line.startsWith("Delta:")) ||
				(!firstDelta && !line.startsWith("Testing Goal:")));
	
		buildDeltaPattern(firstDelta);
	}
	
	private void buildDeltaPattern(boolean firstDelta) {
		if(firstDelta) {
			conflictPattern.setFirstDeltaPattern(currentDelta);
			currentDelta = new DeltaPattern();
		}
		else
			conflictPattern.setSecondDeltaPattern(currentDelta);
	}
	
	private void processTestingGoal(BufferedReader br) throws ApplicationException {
		String targetClassVar = readLine(br);
		
		if(!existsVariable(targetClassVar))
			throw new ApplicationException("Invalid pattern: unknown variable "
					+ targetClassVar  + " in line " + getCurrentLine());
		
		if(!existsClass(targetClassVar))
			throw new ApplicationException("Invalid pattern: unknown class "
					+ targetClassVar + " in line " + getCurrentLine());
		
		TestingGoal goal = new TestingGoal(getClassPattern(targetClassVar));
		
		String line = readLine(br);
		
		while(line != null) {
			
			//ignore blank lines
			if(!line.equals("")) {
				//remove whitespaces
				line = line.replaceAll("\\s", "");
				if(line.matches("\\$[A-Z]\\.\\$[A-Z]")) {
					String[] components = line.split("\\.");
					String callClassVar = components[0];
					String callMethodVar = components[1];
					
					if(!existsVariable(callClassVar))
						throw new ApplicationException("Invalid pattern: unknown variable "
								+ callClassVar  + " in line " + getCurrentLine());
					
					if(!existsClass(callClassVar))
						throw new ApplicationException("Invalid pattern: unknown class "
								+ callClassVar + " in line " + getCurrentLine());
					
					if(!existsVariable(callMethodVar))
						throw new ApplicationException("Invalid pattern: unknown variable "
								+ callMethodVar  + " in line " + getCurrentLine());
					
					if(!existsMethod(callMethodVar))
						throw new ApplicationException("Invalid pattern: unknown method "
								+ callMethodVar + " in line " + getCurrentLine());
					
					ClassPattern callClass = getClassPattern(callClassVar);
					MethodPattern callMethod = definedMethods.get(callMethodVar);
					goal.addMethodToCall(callClass, callMethod);
				}
				
			}
			
			line= readLine(br);
		}
		
		conflictPattern.setTestingGoal(goal);
		
	}

	private void processDeltaLine(String line, boolean firstDelta) throws ApplicationException{
		if(firstDelta && line.startsWith("Testing Goal:"))
			throw new ApplicationException("Missing second delta");
		
		if(!firstDelta && line == null)
			throw new ApplicationException("Missing testing goal");

		//ignore blank lines
		if(isBlankLine(line))
			return ;
		else if(isUpdateAction(line))
			processUpdateAction(line);
		else if(isInsertAction(line))
			processInsertAction(line);
		else
			processEntityAspects(line);

	}
	
	private void processBaseConditionLine(String line) throws ApplicationException {
		if(line == null)
			throw new ApplicationException("Missing first delta");
		//ignore blank lines
		if(isBlankLine(line))
			return ;
		else if(isClassDef(line))
			processClassDef(line);
		else if(isMethodNotDef(line))
			processMethodNotDef(line);
		else
			processEntityAspects(line);
	}
	
	private void processEntityAspects(String line) throws ApplicationException {
		if(isMethodDef(line))
			processMethodDef(line);
		else if(isMethodDependency(line))
			processMethodDependency(line);
	}
	
	private void processClassDef(String line) throws ApplicationException {	
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		
		if(matcher.find()) {
			String var = matcher.group();
			
			if(!existsVariable(var))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ var  + " in line " + getCurrentLine());
			
			if(existsClass(var))
				throw new ApplicationException("Invalid pattern: duplicate class "
						+ var + " in line " + getCurrentLine());
			
			ClassPattern cp = new ClassPattern(variables.get(var));
			definedClasses.put(var, cp);
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processMethodDef(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String classVar = matcher.group();
			
			if(!existsVariable(classVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ classVar  + " in line " + getCurrentLine());
			
			if(!existsClass(classVar))
				throw new ApplicationException("Invalid pattern: undefined class "
						+ classVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String methodVar = matcher.group();
				
				if(!existsVariable(methodVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ methodVar  + " in line " + getCurrentLine());
				
				Visibility vis = null;
				if(isMethodDefWithVisibility(line)) {
					int indexOfHas = line.indexOf("has");
					int indexOfMethod = line.indexOf("method");
					String stringVis = line.substring(indexOfHas + 4, indexOfMethod - 1);
					vis = Visibility.valueOf(stringVis.toUpperCase());
				}
				
				MethodPattern mp = new MethodPattern(variables.get(methodVar), vis);
				getClassPattern(classVar).addMethodPattern(mp);
				definedMethods.put(methodVar, mp);
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processMethodNotDef(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String classVar = matcher.group();
			
			if(!existsVariable(classVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ classVar  + " in line " + getCurrentLine());
			
			if(!existsClass(classVar))
				throw new ApplicationException("Invalid pattern: undefined class "
						+ classVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String methodVar = matcher.group();
				
				if(!existsVariable(methodVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ methodVar  + " in line " + getCurrentLine());
				
				if(existsMethod(methodVar))
					throw new ApplicationException("Invalid pattern: duplicate method "
							+ methodVar + " in line " + getCurrentLine());
				
				getClassPattern(classVar).addExcludedMethod(variables.get(methodVar));
			}
		}
	}
	
	private void processMethodDependency(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String dependantVar = matcher.group();
			
			if(!existsVariable(dependantVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ dependantVar  + " in line " + getCurrentLine());
			
			if(!existsMethod(dependantVar))
				throw new ApplicationException("Invalid pattern: undefined method "
						+ dependantVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String dependencyVar = matcher.group();
				
				if(!existsVariable(dependencyVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ dependencyVar  + " in line " + getCurrentLine());
				
				MethodPattern dependant = definedMethods.get(dependantVar);
				dependant.addDependency(variables.get(dependencyVar));
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processUpdateAction(String line) throws ApplicationException {
		if(isUpdateMethodAction(line)) {
			Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
			if(matcher.find()) {
				String methodVar = matcher.group();
				
				if(!existsVariable(methodVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ methodVar  + " in line " + getCurrentLine());
				
				if(!existsMethod(methodVar))
					throw new ApplicationException("Invalid pattern: undefined method "
							+ methodVar + " in line " + getCurrentLine());
				
				UpdateMethodPatternAction umpa =
						new UpdateMethodPatternAction(definedMethods.get(methodVar));
				currentDelta.addActionPattern(umpa);
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
	}
	
	private void processInsertAction(String line) throws ApplicationException{
		if(isInsertClassAction(line))
			processClassInsert(line);
		else if(isInsertMethodAction(line))
			processMethodInsert(line);
		else if(isInsertDependencyAction(line))
			processDependencyInsert(line);
	}
	
	private void processClassInsert(String line) throws ApplicationException{
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String classVar = matcher.group();
			
			if(!existsVariable(classVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ classVar  + " in line " + getCurrentLine());
			
			if(existsClass(classVar))
				throw new ApplicationException("Invalid pattern: duplicate class "
						+ classVar + " in line " + getCurrentLine());
			
			ClassPattern classPattern = new ClassPattern(variables.get(classVar));
			insertedClasses.put(classVar, classPattern);
			
			InsertClassPatternAction icpa = 
					new InsertClassPatternAction(classPattern);
			currentDelta.addActionPattern(icpa);
		}
	}
	
	private void processMethodInsert(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
			if(existsMethod(methodVar))
				throw new ApplicationException("Invalid pattern: duplicate method "
						+ methodVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsVariable(classVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ classVar  + " in line " + getCurrentLine());
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				Visibility visibility = null;
				if(isInsertMethodWithVisibility(line)) {
					int indexOfHas = line.indexOf("Insert");
					int indexOfMethod = line.indexOf("method");
					String stringVis = line.substring(indexOfHas + 7, indexOfMethod - 1);
					visibility = Visibility.valueOf(stringVis.toUpperCase());
				}
				
				MethodPattern methodPattern = 
						new MethodPattern(variables.get(methodVar), visibility);
				definedMethods.put(methodVar, methodPattern);
				
				InsertMethodPatternAction impa = 
						new InsertMethodPatternAction(methodPattern, 
								getClassPattern(classVar));
				currentDelta.addActionPattern(impa);
			}
		}
	}
	
	private void processDependencyInsert(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String dependencyVar = matcher.group();
			if(!existsVariable(dependencyVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ dependencyVar  + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String dependantVar = matcher.group();
				if(!existsVariable(dependantVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ dependantVar  + " in line " + getCurrentLine());
				
				if(!existsMethod(dependantVar))
					throw new ApplicationException("Invalid pattern: undefined method "
							+ dependantVar + " in line " + getCurrentLine());
				
				MethodInvocationPattern mip = 
						new MethodInvocationPattern(variables.get(dependencyVar));
				
				InsertInvocationPatternAction iipa = 
						new InsertInvocationPatternAction(mip, 
								definedMethods.get(dependantVar));
				
				currentDelta.addActionPattern(iipa);
			}
		}
	}
	
	private boolean isBlankLine(String line) {
		return line.equals("");
	}
	
	private boolean isClassDef(String line) {
		return line.matches("Class " + VAR_PATTERN +"\\s*");
	}
	
	private boolean isUpdateAction(String line) {
		return line.startsWith("Update");
	}
	
	private boolean isInsertAction(String line) {
		return line.startsWith("Insert");
	}
	
	private boolean isUpdateMethodAction(String line) {
		return line.matches("Update method " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertClassAction(String line) {
		return line.matches("Insert class " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertDependencyAction(String line) {
		return line.matches("Insert dependency to method " + VAR_PATTERN + 
				" in method " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertMethodAction(String line) {
		return isInsertMethodWithoutVisibility(line) ||
				isInsertMethodWithVisibility(line);
	}
	
	private boolean isInsertMethodWithoutVisibility(String line) {
		return line.matches("Insert method " + VAR_PATTERN + " in class " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertMethodWithVisibility(String line) {
		return line.matches("Insert (public|private|package|protected) " + 
				"method " + VAR_PATTERN + " in class " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isMethodDef(String line) {
		return isMethodDefWithoutVisibility(line) ||
				isMethodDefWithVisibility(line);
	}
	
	private boolean isMethodDefWithoutVisibility(String line) {
		return line.matches("Class " + VAR_PATTERN + " has method " +
					VAR_PATTERN + "\\s*");
	}
	
	private boolean isMethodDefWithVisibility(String line) {
		return line.matches("Class " + VAR_PATTERN + " has "
				+ "(public|private|package|protected) method " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isMethodNotDef(String line) {
		return line.matches("Class " + VAR_PATTERN + " does not have method " + 
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isMethodDependency(String line) {
		return line.matches("Method " + VAR_PATTERN + " depends on method " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isDifferentVariableRule(String line) {
		return line.matches(VAR_PATTERN + " different from " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isCanBeEqualVariableRule(String line) {
		return line.matches(VAR_PATTERN + " can be equal to " + VAR_PATTERN + "\\s*");
	}
	
	public static void main(String[] args) throws ApplicationException {
		String filePath = "src" + File.separator + "main" + File.separator + 
				"resources" + File.separator + "conflict-patterns" + 
				File.separator + "UnexpectedOverridingNewDependency.co";
		
		ConflictPattern cp = PatternParser.getConflictPattern(filePath);
		System.out.println(cp.toString());
	}
}
