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

import matcher.entities.FieldAccessType;
import matcher.entities.Visibility;
import matcher.entities.deltas.Action;
import matcher.exceptions.ApplicationException;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.InterfacePattern;
import matcher.patterns.MethodInvocationPattern;
import matcher.patterns.MethodPattern;
import matcher.patterns.TypePattern;
import matcher.patterns.deltas.ActionPattern;
import matcher.patterns.deltas.DeleteConstructorPatternAction;
import matcher.patterns.deltas.DeleteFieldAccessPatternAction;
import matcher.patterns.deltas.DeleteFieldPatternAction;
import matcher.patterns.deltas.DeleteInvocationPatternAction;
import matcher.patterns.deltas.DeleteMethodPatternAction;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertClassPatternAction;
import matcher.patterns.deltas.InsertConstructorPatternAction;
import matcher.patterns.deltas.InsertFieldAccessPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertInvocationPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.deltas.UpdateConstructorPatternAction;
import matcher.patterns.deltas.UpdateDependencyPatternAction;
import matcher.patterns.deltas.UpdateFieldTypePatternAction;
import matcher.patterns.deltas.UpdateMethodPatternAction;
import matcher.patterns.deltas.VisibilityActionPattern;
import matcher.patterns.goals.TestingGoal;

public class PatternParser {
	
	private static final String VAR_PATTERN = "\\$[A-Z]([1-9]+)?";
	private static final String VISIBILITY_PATTERN = "(public|private|package|protected)";
	private static final Pattern VAR_REGEX_PATTERN = Pattern.compile(VAR_PATTERN);
	private static final Pattern VISIBILITY_REGEX_PATTERN = Pattern.compile(VISIBILITY_PATTERN);
		
	private Map<String, FreeVariable> variables;
	
	private Map<String, ClassPattern> definedClasses;
	private Map<String, MethodPattern> definedMethods;
	private Map<String, ConstructorPattern> definedConstructors;
	private Map<String, FieldPattern> definedFields;
	private Map<String, InterfacePattern> definedInterfaces;
	
	private Map<String, ClassPattern> methodOwners;
	
	private Map<String, ClassPattern> insertedClasses;
	
	private int lineNumber;
	
	private ConflictPattern conflictPattern;
	private DeltaPattern currentDelta;
	private boolean processingDeltas;
	private ActionPattern lastAction;
	
	public PatternParser() {
		lineNumber = 1;
		variables = new HashMap<>();
		definedClasses = new HashMap<>();
		definedMethods = new HashMap<>();
		definedConstructors = new HashMap<>();
		definedFields = new HashMap<>();
		definedInterfaces = new HashMap<>();
		
		methodOwners = new HashMap<>();
		
		insertedClasses = new HashMap<>();
		
		currentDelta = new DeltaPattern();
		processingDeltas = false;
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
		
		processConstraints(br);
		
		processBasePattern(br);
		processingDeltas = true;
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
	
	private boolean existsInterface(String variable) {
		return definedInterfaces.containsKey(variable);
	}
	
	private boolean existsField(String variable) {
		return definedFields.containsKey(variable);
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
	
	private boolean existsConstructor(String variable) {
		return definedConstructors.containsKey(variable);
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
	
	private void processConstraints(BufferedReader br) throws ApplicationException {
		boolean existRules = findConstraints(br);
		if(existRules) {
			String line;
			try {
				do{
					line = readLine(br);
					
					if(!line.equals("") && isCanBeEqualVariableRule(line))
						processCanBeEqualVariableRule(line);
					
				}while(!line.startsWith("Base Condition:"));
			}
			catch (NullPointerException e) {
				throw new ApplicationException("Missing base condition");	
			} 
		}
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
	
	private boolean findConstraints(BufferedReader br) throws ApplicationException {
		String line;
		try {
			do{
				line = readLine(br);
			}while(!line.startsWith("Constraints:") &&
					!line.startsWith("Base Condition:"));
		}
		catch (NullPointerException e) {
			throw new ApplicationException("Missing base condition");	
		} 
		return line.startsWith("Constraints:");
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
		for(Map.Entry<String, InterfacePattern> e: definedInterfaces.entrySet()) {
			basePattern.addInterfacePattern(e.getValue());
		}
		
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

				if(line.matches(VAR_PATTERN + "\\." + VAR_PATTERN)) {
					String[] components = line.split("\\.");
					String callClassVar = components[0];
					String callVar = components[1];
					
					if(!existsVariable(callClassVar))
						throw new ApplicationException("Invalid pattern: unknown variable "
								+ callClassVar  + " in line " + getCurrentLine());
					
					if(!existsClass(callClassVar))
						throw new ApplicationException("Invalid pattern: unknown class "
								+ callClassVar + " in line " + getCurrentLine());
					
					if(!existsVariable(callVar))
						throw new ApplicationException("Invalid pattern: unknown variable "
								+ callVar  + " in line " + getCurrentLine());
					
					if(!existsMethod(callVar) && !existsConstructor(callVar))
						throw new ApplicationException("Invalid pattern: unknown method/constructor "
								+ callVar + " in line " + getCurrentLine());
					
					ClassPattern callClass = getClassPattern(callClassVar);
					if(existsMethod(callVar)) {
						MethodPattern callMethod = definedMethods.get(callVar);
						goal.addMethodToCall(callClass, callMethod);
					}
					else {
						ConstructorPattern callConstructor = definedConstructors.get(callVar);
						goal.addConstructorToCall(callClass, callConstructor);
					}
					
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
		else if(isDeleteAction(line))
			processDeleteAction(line);
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
		else if(isInterfaceDef(line))
			processInterfaceDef(line);
		else
			processEntityAspects(line);
	}
	
	private void processEntityAspects(String line) throws ApplicationException {
		if(isMethodDef(line))
			processMethodDef(line);
		else if(isConstructorDef(line))
			processConstructorDef(line);
		else if(isMethodNotDef(line))
			processMethodNotDef(line);
		else if(isMethodDependency(line))
			processMethodDependency(line);
		else if(isMethodCompatibility(line))
			processMethodCompatibility(line);
		else if(isExtendsDef(line))
			processExtendsDef(line);
		else if(isFieldDef(line))
			processFieldDef(line);
		else if(isFieldUse(line))
			processFieldUse(line);
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
	
	private void processInterfaceDef(String line) throws ApplicationException{
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		
		if(matcher.find()) {
			String var = matcher.group();
			if(!existsVariable(var))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ var  + " in line " + getCurrentLine());
			
			if(existsInterface(var))
				throw new ApplicationException("Invalid pattern: duplicate interface "
						+ var + " in line " + getCurrentLine());
			
			InterfacePattern ip = new InterfacePattern(variables.get(var));
			definedInterfaces.put(var, ip);
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processFieldDef(String line) throws ApplicationException {
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
				String fieldVar = matcher.group();
				
				if(!existsVariable(fieldVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ fieldVar  + " in line " + getCurrentLine());
				
				Visibility vis = null;
				if(isFieldDefWithVisibility(line)) {
					int indexOfHas = line.indexOf("has");
					int indexOfMethod = line.indexOf("field");
					String stringVis = line.substring(indexOfHas + 4, indexOfMethod - 1);
					vis = Visibility.valueOf(stringVis.toUpperCase());
				}
				
				FieldPattern fieldPattern = 
						new FieldPattern(variables.get(fieldVar), vis);
				
				
				if(isFieldDefWithType(line)) {
					int indexOfType = line.indexOf("type");
					String typeVar = line.substring(indexOfType+5)
							.replaceAll("\\s", "");
					
					if(!existsVariable(typeVar))
						throw new ApplicationException("Invalid pattern: unknown variable "
								+ typeVar  + " in line " + getCurrentLine());
					
					if(!existsClass(typeVar) && !existsInterface(typeVar))
						throw new ApplicationException("Invalid pattern: undefined type "
								+ typeVar + " in line " + getCurrentLine());
					
					TypePattern typePattern = new TypePattern(variables.get(typeVar));
					fieldPattern.setType(typePattern);
				}
				
				getClassPattern(classVar).addFieldPattern(fieldPattern);
				
				definedFields.put(fieldVar, fieldPattern);
			}
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processFieldUse(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
			if(!existsMethod(methodVar))
				throw new ApplicationException("Invalid pattern: undefined method "
						+ methodVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String fieldVar = matcher.group();
				
				if(!existsVariable(fieldVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ fieldVar  + " in line " + getCurrentLine());
				
				if(!existsField(fieldVar))
					throw new ApplicationException("Invalid pattern: undefined field "
							+ fieldVar + " in line " + getCurrentLine());
				
				FieldAccessType accessType = null;
				if(isFieldRead(line))
					accessType = FieldAccessType.READ;
				else if(isFieldWrite(line))
					accessType = FieldAccessType.WRITE;
				
				FieldAccessPattern accessPattern = 
						new FieldAccessPattern(variables.get(fieldVar), accessType);
				
				definedMethods.get(methodVar).addFieldAccessPattern(accessPattern);
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processExtendsDef(String line) throws ApplicationException {
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
				String superClassVar = matcher.group();
				
				if(!existsVariable(superClassVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ superClassVar  + " in line " + getCurrentLine());
				
				if(!existsClass(superClassVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ superClassVar + " in line " + getCurrentLine());
				
				ClassPattern classPattern = getClassPattern(classVar);
				ClassPattern superClassPattern = getClassPattern(superClassVar);
				classPattern.setSuperClass(superClassPattern);
				
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
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
				methodOwners.put(methodVar, getClassPattern(classVar));
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processConstructorDef(String line) throws ApplicationException {
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
				String constructorVar = matcher.group();
				
				if(!existsVariable(constructorVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ constructorVar  + " in line " + getCurrentLine());
				
				Visibility vis = null;
				if(isConstructorDefWithVisibility(line)) {
					int indexOfHas = line.indexOf("has");
					int indexOfMethod = line.indexOf("constructor");
					String stringVis = line.substring(indexOfHas + 4, indexOfMethod - 1);
					vis = Visibility.valueOf(stringVis.toUpperCase());
				}
				
				ConstructorPattern cp = new ConstructorPattern(variables.get(constructorVar), vis);
				getClassPattern(classVar).addConstructorPattern(cp);
				definedConstructors.put(constructorVar, cp);
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
	
	private void processMethodCompatibility(String line) 
			throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
			if(!existsMethod(methodVar))
				throw new ApplicationException("Invalid pattern: undefined method "
						+ methodVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String compatibleMethodVar = matcher.group();
				
				if(!existsVariable(compatibleMethodVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ compatibleMethodVar  + " in line " + getCurrentLine());
				
				if(!existsMethod(compatibleMethodVar))
					throw new ApplicationException("Invalid pattern: undefined method "
							+ compatibleMethodVar + " in line " + getCurrentLine());
				
				if(!processingDeltas) {
					ClassPattern classPattern = methodOwners.get(methodVar);
					classPattern.addCompatible(variables.get(methodVar), 
							variables.get(compatibleMethodVar));
				}
				else if(lastAction instanceof InsertMethodPatternAction){
					InsertMethodPatternAction impa = 
							(InsertMethodPatternAction) lastAction;
					impa.addCompatible(definedMethods.get(compatibleMethodVar));
				}
				
			}
		}
	}
	
	private void processUpdateAction(String line) throws ApplicationException {
		if(isUpdateMethodAction(line))
			processUpdateMethodAction(line);
		else if(isUpdateConstructorAction(line))
			processUpdateConstructorAction(line);
		else if(isUpdateVisibilityAction(line))
			processUpdateVisibilityAction(line);
		else if(isUpdateFieldType(line))
			processUpdateFieldTypeAction(line);
		else if(isUpdateDependency(line))
			processUpdateDependencyAction(line);
	}
	
	private void processUpdateMethodAction(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
			if(!existsMethod(methodVar))
				throw new ApplicationException("Invalid pattern: undefined method "
						+ methodVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				ClassPattern classPattern = getClassPattern(classVar);
				UpdateMethodPatternAction umpa =
						new UpdateMethodPatternAction(
								definedMethods.get(methodVar), classPattern);
				currentDelta.addActionPattern(umpa);
				lastAction = umpa;
			}
			else 
				throw new ApplicationException("Missing class in " 
						+ getCurrentLine());
			
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processUpdateConstructorAction(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String constructorVar = matcher.group();
			
			if(!existsVariable(constructorVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ constructorVar  + " in line " + getCurrentLine());
			
			if(!existsConstructor(constructorVar))
				throw new ApplicationException("Invalid pattern: undefined constructor "
						+ constructorVar + " in line " + getCurrentLine());
			
			UpdateConstructorPatternAction ucpa =
					new UpdateConstructorPatternAction(definedConstructors.get(constructorVar));
			currentDelta.addActionPattern(ucpa);
			
			lastAction = ucpa;
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processUpdateVisibilityAction(String line) 
			throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
			if(!existsMethod(methodVar))
				throw new ApplicationException("Invalid pattern: undefined method "
						+ methodVar + " in line " + getCurrentLine());
			
			Matcher visMatcher = VISIBILITY_REGEX_PATTERN.matcher(line);
			if(visMatcher.find()) {
				String visString = visMatcher.group();
				Visibility newVisibility = 
						Visibility.valueOf(visString.toUpperCase());
				
				MethodPattern methodPattern = definedMethods.get(methodVar);
				
				Visibility oldVisibility = methodPattern.getVisibility();
				
				VisibilityActionPattern vap = 
						new VisibilityActionPattern(Action.UPDATE, newVisibility, 
								oldVisibility, variables.get(methodVar));
				
				currentDelta.addActionPattern(vap);
				lastAction = vap;
				
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processUpdateFieldTypeAction(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String fieldVar = matcher.group();
			
			if(!existsVariable(fieldVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ fieldVar  + " in line " + getCurrentLine());
			
			if(!existsField(fieldVar))
				throw new ApplicationException("Invalid pattern: undefined field "
						+ fieldVar + " in line " + getCurrentLine());
			
			FieldPattern fieldPattern = definedFields.get(fieldVar);
			TypePattern newTypePattern = null;
			if(matcher.find()) {
				String typeVar = matcher.group();
				
				if(!existsVariable(typeVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ typeVar  + " in line " + getCurrentLine());
				
				if(!existsClass(typeVar) && !existsInterface(typeVar))
					throw new ApplicationException("Invalid pattern: undefined type "
							+ typeVar + " in line " + getCurrentLine());
				
				
				newTypePattern = new TypePattern(variables.get(typeVar));				
			}
			UpdateFieldTypePatternAction uftpa = 
					new UpdateFieldTypePatternAction(fieldPattern, newTypePattern);
			
			currentDelta.addActionPattern(uftpa);
			lastAction = uftpa;
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processUpdateDependencyAction(String line) 
			throws ApplicationException{
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String oldMethodDependencyVar = matcher.group();
			
			if(!existsVariable(oldMethodDependencyVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ oldMethodDependencyVar  + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String newMethodDependencyVar = matcher.group();
				
				if(!existsVariable(newMethodDependencyVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ newMethodDependencyVar  + " in line " + getCurrentLine());
				
				if(matcher.find()) {
					String var = matcher.group();
					
					if(!existsVariable(var))
						throw new ApplicationException("Invalid pattern: unknown variable "
								+ var  + " in line " + getCurrentLine());
					
					if(!existsMethod(var) && !existsConstructor(var))
						throw new ApplicationException("Invalid pattern: undefined method/constructor "
								+ var + " in line " + getCurrentLine());
					
					UpdateDependencyPatternAction udpa;
					if(existsMethod(var)) {
						MethodPattern methodPattern = definedMethods.get(var);
						
						udpa = new UpdateDependencyPatternAction(methodPattern, 
										variables.get(oldMethodDependencyVar), 
										variables.get(newMethodDependencyVar));
					}
					else {
						ConstructorPattern cPattern = definedConstructors.get(var);
						
						udpa = new UpdateDependencyPatternAction(cPattern, 
								variables.get(oldMethodDependencyVar), 
								variables.get(newMethodDependencyVar));
					}
					
					currentDelta.addActionPattern(udpa);
					lastAction = udpa;
				}
				else 
					throw new ApplicationException("Something went wrong reading line " 
							+ getCurrentLine());
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processInsertAction(String line) throws ApplicationException{
		if(isInsertClassAction(line))
			processClassInsert(line);
		else if(isInsertMethodAction(line))
			processMethodInsert(line);
		else if(isInsertDependencyAction(line))
			processDependencyInsert(line);
		else if(isInsertFieldAction(line))
			processFieldInsert(line);
		else if(isInsertConstructorAction(line))
			processConstructorInsert(line);
		else if(isInsertFieldAccessAction(line))
			processFieldAccessInsert(line);
	}
	
	private void processDeleteAction(String line) throws ApplicationException{
		if(isDeleteMethodAction(line))
			processDeleteMethodAction(line);
		else if(isDeleteConstructorAction(line))
			processDeleteConstructorAction(line);
		else if(isDeleteFieldAction(line))
			processDeleteFieldAction(line);
		else if(isDeleteFieldAccessAction(line))
			processDeleteFieldAccessAction(line);
		else if(isDeleteDependencyAction(line))
			processDeleteDependencyAction(line);
	}
	
	private void processDeleteMethodAction(String line) 
			throws ApplicationException{
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
			if(!existsMethod(methodVar))
				throw new ApplicationException("Invalid pattern: undefined method "
						+ methodVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsVariable(classVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ classVar  + " in line " + getCurrentLine());
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				MethodPattern methodPattern = definedMethods.get(methodVar);
				ClassPattern classPattern = getClassPattern(classVar);
				
				DeleteMethodPatternAction dmpa = 
						new DeleteMethodPatternAction(methodPattern, classPattern);
				
				currentDelta.addActionPattern(dmpa);
				lastAction = dmpa;
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processDeleteConstructorAction(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String constructorVar = matcher.group();
			
			if(!existsVariable(constructorVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ constructorVar  + " in line " + getCurrentLine());
			
			if(!existsConstructor(constructorVar))
				throw new ApplicationException("Invalid pattern: undefined constructor "
						+ constructorVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsVariable(classVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ classVar  + " in line " + getCurrentLine());
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				ConstructorPattern cPattern = definedConstructors.get(constructorVar);
				ClassPattern classPattern = getClassPattern(classVar);
				
				DeleteConstructorPatternAction dcpa = 
						new DeleteConstructorPatternAction(cPattern, classPattern);
				
				currentDelta.addActionPattern(dcpa);
				lastAction = dcpa;
				
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processDeleteFieldAction(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String fieldVar = matcher.group();
			
			if(!existsVariable(fieldVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ fieldVar  + " in line " + getCurrentLine());
			
			if(!existsField(fieldVar))
				throw new ApplicationException("Invalid pattern: undefined field "
						+ fieldVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsVariable(classVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ classVar  + " in line " + getCurrentLine());
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				FieldPattern fieldPattern = definedFields.get(fieldVar);
				ClassPattern classPattern = getClassPattern(classVar);
				
				DeleteFieldPatternAction dfpa = 
						new DeleteFieldPatternAction(fieldPattern, classPattern);
				
				currentDelta.addActionPattern(dfpa);
				lastAction = dfpa;
				
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processDeleteFieldAccessAction(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String fieldVar = matcher.group();
			
			if(!existsVariable(fieldVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ fieldVar  + " in line " + getCurrentLine());
			
			if(!existsField(fieldVar))
				throw new ApplicationException("Invalid pattern: undefined field "
						+ fieldVar + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String methodVar = matcher.group();
				
				if(!existsVariable(methodVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ methodVar  + " in line " + getCurrentLine());
				
				if(!existsMethod(methodVar))
					throw new ApplicationException("Invalid pattern: undefined method "
							+ methodVar + " in line " + getCurrentLine());
				
				FieldAccessType accessType = null;
				if(isDeleteFieldReadAction(line))
					accessType = FieldAccessType.READ;
				else if(isDeleteFieldWriteAction(line))
					accessType = FieldAccessType.WRITE;
				
				FieldAccessPattern accessPattern = 
						new FieldAccessPattern(variables.get(fieldVar), accessType);
				
				DeleteFieldAccessPatternAction dfpa = 
						new DeleteFieldAccessPatternAction(accessPattern, 
								definedMethods.get(methodVar));
						
				currentDelta.addActionPattern(dfpa);
				lastAction = dfpa;
			}
			else
				throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
		}
		else
			throw new ApplicationException("Something went wrong reading line " 
				+ getCurrentLine());
	}
	
	private void processDeleteDependencyAction(String line) throws ApplicationException {
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
				
				if(!existsMethod(dependantVar) && !existsConstructor(dependantVar))
					throw new ApplicationException("Invalid pattern: undefined method/constructor "
							+ dependantVar + " in line " + getCurrentLine());
				
				MethodInvocationPattern mip = 
						new MethodInvocationPattern(variables.get(dependencyVar));
				
				DeleteInvocationPatternAction dipa;
				
				if(existsMethod(dependantVar)) {
					dipa = new DeleteInvocationPatternAction(mip, 
									definedMethods.get(dependantVar));
				}
				else {
					dipa = new DeleteInvocationPatternAction(mip, 
							definedConstructors.get(dependantVar));
				}
				
				currentDelta.addActionPattern(dipa);
				lastAction = dipa;
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
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
			
			lastAction = icpa;
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processMethodInsert(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String methodVar = matcher.group();
			
			if(!existsVariable(methodVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ methodVar  + " in line " + getCurrentLine());
			
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
				
				ClassPattern classPattern = getClassPattern(classVar);
				
				if(classPattern.excludesMethod(variables.get(methodVar))) {
					ClassPattern clone = new ClassPattern(classPattern);
					clone.removeExcludedMethod(variables.get(methodVar));
					classPattern = clone;
				}
				
				MethodPattern methodPattern = 
						new MethodPattern(variables.get(methodVar), visibility);
				definedMethods.put(methodVar, methodPattern);
				methodOwners.put(methodVar, classPattern);
				
				InsertMethodPatternAction impa = 
						new InsertMethodPatternAction(methodPattern, classPattern);
				currentDelta.addActionPattern(impa);
				
				lastAction = impa;
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processFieldAccessInsert(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String fieldVar = matcher.group();
			
			if(!existsField(fieldVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ fieldVar  + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String methodVar = matcher.group();
				
				if(!existsVariable(methodVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ methodVar  + " in line " + getCurrentLine());
				
				if(!existsMethod(methodVar))
					throw new ApplicationException("Invalid pattern: undefined method "
							+ methodVar + " in line " + getCurrentLine());
				
				FieldAccessType access = null;
				if(isInsertFieldReadAction(line))
					access = FieldAccessType.READ;
				else if(isInsertFieldWriteAction(line))
					access = FieldAccessType.WRITE;
				
				FieldAccessPattern accessPattern = 
						new FieldAccessPattern(variables.get(fieldVar), access);
				
				InsertFieldAccessPatternAction ifpa = 
						new InsertFieldAccessPatternAction(accessPattern, 
								definedMethods.get(methodVar));
						
				currentDelta.addActionPattern(ifpa);
				lastAction = ifpa;
			}
			else
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processConstructorInsert(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String constructorVar = matcher.group();
			
			if(!existsVariable(constructorVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ constructorVar  + " in line " + getCurrentLine());
			
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsVariable(classVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ classVar  + " in line " + getCurrentLine());
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				Visibility visibility = null;
				if(isInsertConstructorWithVisibility(line)) {
					int indexOfHas = line.indexOf("Insert");
					int indexOfMethod = line.indexOf("constructor");
					String stringVis = line.substring(indexOfHas + 7, indexOfMethod - 1);
					visibility = Visibility.valueOf(stringVis.toUpperCase());
				}
				
				ClassPattern classPattern = getClassPattern(classVar);
				ConstructorPattern cPattern = 
						new ConstructorPattern(variables.get(constructorVar), visibility);
				
				definedConstructors.put(constructorVar, cPattern);
				
				InsertConstructorPatternAction icpa = 
						new InsertConstructorPatternAction(cPattern, classPattern);
				currentDelta.addActionPattern(icpa);
				lastAction = icpa;
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
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
				
				if(!existsMethod(dependantVar) && !existsConstructor(dependantVar))
					throw new ApplicationException("Invalid pattern: undefined method/constructor "
							+ dependantVar + " in line " + getCurrentLine());
				
				MethodInvocationPattern mip = 
						new MethodInvocationPattern(variables.get(dependencyVar));
				
				InsertInvocationPatternAction iipa;
				
				if(existsMethod(dependantVar)) {
					iipa = new InsertInvocationPatternAction(mip, 
									definedMethods.get(dependantVar));
				}
				else {
					iipa = new InsertInvocationPatternAction(mip, 
							definedConstructors.get(dependantVar));
				}
				
				currentDelta.addActionPattern(iipa);
				lastAction = iipa;
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private void processFieldInsert(String line) throws ApplicationException {
		Matcher matcher = VAR_REGEX_PATTERN.matcher(line);
		if(matcher.find()) {
			String fieldVar = matcher.group();
			if(!existsVariable(fieldVar))
				throw new ApplicationException("Invalid pattern: unknown variable "
						+ fieldVar  + " in line " + getCurrentLine());
		
			if(matcher.find()) {
				String classVar = matcher.group();
				
				if(!existsVariable(classVar))
					throw new ApplicationException("Invalid pattern: unknown variable "
							+ classVar  + " in line " + getCurrentLine());
				
				if(!existsClass(classVar))
					throw new ApplicationException("Invalid pattern: undefined class "
							+ classVar + " in line " + getCurrentLine());
				
				Visibility visibility = null;
				if(isInsertFieldWithVisibility(line)) {
					int indexOfHas = line.indexOf("Insert");
					int indexOfMethod = line.indexOf("field");
					String stringVis = line.substring(indexOfHas + 7, indexOfMethod - 1);
					visibility = Visibility.valueOf(stringVis.toUpperCase());
				}
				
				FieldPattern fieldPattern = 
						new FieldPattern(variables.get(fieldVar), visibility);
				
				InsertFieldPatternAction ifpa = 
						new InsertFieldPatternAction(fieldPattern, 
								getClassPattern(classVar));
				
				definedFields.put(fieldVar, fieldPattern);
				currentDelta.addActionPattern(ifpa);
				
				lastAction = ifpa;
				
			}
			else 
				throw new ApplicationException("Something went wrong reading line " 
						+ getCurrentLine());
		}
		else 
			throw new ApplicationException("Something went wrong reading line " 
					+ getCurrentLine());
	}
	
	private boolean isBlankLine(String line) {
		return line.equals("");
	}
	
	private boolean isClassDef(String line) {
		return line.matches("Class " + VAR_PATTERN +"\\s*");
	}
	
	private boolean isInterfaceDef(String line) {
		return line.matches("Interface " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isExtendsDef(String line) {
		return line.matches("Class " + VAR_PATTERN + " extends class " + 
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isUpdateAction(String line) {
		return line.startsWith("Update");
	}
	
	private boolean isInsertAction(String line) {
		return line.startsWith("Insert");
	}
	
	private boolean isDeleteAction(String line) {
		return line.startsWith("Delete");
	}
	
	private boolean isDeleteMethodAction(String line) {
		return line.matches("Delete method " + VAR_PATTERN + 
				" from class " + VAR_PATTERN);
	}
	
	private boolean isDeleteConstructorAction(String line) {
		return line.matches("Delete constructor " + VAR_PATTERN + 
				" from class " + VAR_PATTERN);
	}
	
	private boolean isDeleteFieldAction(String line) {
		return line.matches("Delete field " + VAR_PATTERN + 
				" from class " + VAR_PATTERN);
	}
	
	private boolean isDeleteFieldAccessAction(String line) {
		return isDeleteFieldReadAction(line) ||
				isDeleteFieldWriteAction(line) ||
				isDeleteFieldUseAction(line);
	}
	
	private boolean isDeleteFieldReadAction(String line) {
		return line.matches("Delete field read of field " + 
				VAR_PATTERN + " in method " + VAR_PATTERN);
	}
	
	private boolean isDeleteFieldWriteAction(String line) {
		return line.matches("Delete field write of field " + 
				VAR_PATTERN + " in method " + VAR_PATTERN);
	}
	
	private boolean isDeleteFieldUseAction(String line) {
		return line.matches("Delete field use of field " + 
				VAR_PATTERN + " in method " + VAR_PATTERN);
	}
	
	private boolean isDeleteDependencyAction(String line) {
		return line.matches("Delete dependency to method " + VAR_PATTERN + 
				" in (method|constructor) " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isUpdateMethodAction(String line) {
		return line.matches("Update method " + VAR_PATTERN + 
				" of class " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isUpdateConstructorAction(String line) {
		return line.matches("Update constructor " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isUpdateVisibilityAction(String line) {
		return line.matches("Update visibility of method " + VAR_PATTERN + 
				" to " + VISIBILITY_PATTERN + "\\s*");
	}
	
	private boolean isUpdateFieldType(String line) {
		return line.matches("Update field type of field " + VAR_PATTERN + 
				" to " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isUpdateDependency(String line) {
		return line.matches("Update dependency from method " + VAR_PATTERN + 
				" to method " + VAR_PATTERN + " in (method|constructor) " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertClassAction(String line) {
		return line.matches("Insert class " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertDependencyAction(String line) {
		return line.matches("Insert dependency to method " + VAR_PATTERN + 
				" in (method|constructor) " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertFieldAction(String line) {
		return isInsertFieldWithoutVisibility(line) ||
				isInsertFieldWithVisibility(line);
	}
	
	private boolean isInsertFieldWithoutVisibility(String line) {
		return line.matches("Insert field " + VAR_PATTERN + " in class " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertFieldWithVisibility(String line) {
		return line.matches("Insert " + VISIBILITY_PATTERN + 
				" field " + VAR_PATTERN + " in class " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertFieldAccessAction(String line) {
		return isInsertFieldReadAction(line) ||
				isInsertFieldWriteAction(line) ||
				isInsertFieldUseAction(line);
	}
	
	private boolean isInsertFieldReadAction(String line) {
		return line.matches("Insert field read of field " + 
				VAR_PATTERN + " in method " + VAR_PATTERN);
	}
	
	private boolean isInsertFieldWriteAction(String line) {
		return line.matches("Insert field write of field " + 
				VAR_PATTERN + " in method " + VAR_PATTERN);
	}
	
	private boolean isInsertFieldUseAction(String line) {
		return line.matches("Insert field use of field " + 
				VAR_PATTERN + " in method " + VAR_PATTERN);
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
		return line.matches("Insert " + VISIBILITY_PATTERN + 
				" method " + VAR_PATTERN + " in class " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertConstructorAction(String line) {
		return isInsertConstructorWithoutVisibility(line) ||
				isInsertConstructorWithVisibility(line);
	}
	
	private boolean isInsertConstructorWithoutVisibility(String line) {
		return line.matches("Insert constructor " + VAR_PATTERN + " in class " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isInsertConstructorWithVisibility(String line) {
		return line.matches("Insert " + VISIBILITY_PATTERN + 
				" constructor " + VAR_PATTERN + " in class " +
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
				+ VISIBILITY_PATTERN + " method " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isConstructorDef(String line) {
		return isConstructorDefWithoutVisibility(line) ||
				isConstructorDefWithVisibility(line);
	}
	
	private boolean isConstructorDefWithoutVisibility(String line) {
		return line.matches("Class " + VAR_PATTERN + " has constructor " +
					VAR_PATTERN + "\\s*");
	}
	
	private boolean isConstructorDefWithVisibility(String line) {
		return line.matches("Class " + VAR_PATTERN + " has "
				+ VISIBILITY_PATTERN + " constructor " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isFieldDef(String line) {
		return isFieldDefWithoutVisibility(line) ||
				isFieldDefWithVisibility(line);
	}

	private boolean isFieldDefWithoutVisibility(String line) {
		return line.matches("Class " + VAR_PATTERN + " has field " +
				VAR_PATTERN + ".*\\s*");
	}

	private boolean isFieldDefWithVisibility(String line) {
		return line.matches("Class " + VAR_PATTERN + " has "
				+  VISIBILITY_PATTERN + " field " +
				VAR_PATTERN + ".*\\s*");
	}
	
	private boolean isFieldDefWithType(String line) {
		return line.matches("Class " + VAR_PATTERN + " has ("
				+  VISIBILITY_PATTERN + " )?field " +
				VAR_PATTERN + " of type " + VAR_PATTERN + "\\s*");
	}
	
	private boolean isFieldUse(String line) {
		return isFieldRead(line) || isFieldWrite(line) || isFieldAnyUse(line);
	}
	
	private boolean isFieldRead(String line) {
		return line.matches("Method " + VAR_PATTERN + " reads field " + 
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isFieldWrite(String line) {
		return line.matches("Method " + VAR_PATTERN + " writes field " + 
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isFieldAnyUse(String line) {
		return line.matches("Method " + VAR_PATTERN + " uses field " + 
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
	
	private boolean isMethodCompatibility(String line) {
		return line.matches("Method " + VAR_PATTERN + " compatible with method " +
				VAR_PATTERN + "\\s*");
	}
	
	private boolean isCanBeEqualVariableRule(String line) {
		return line.matches(VAR_PATTERN + " can be equal to " + VAR_PATTERN + "\\s*");
	}
	
//	public static void main(String[] args) throws ApplicationException {
//		String dirPath = "src" + File.separator + "main" + File.separator + 
//				"resources" + File.separator + "conflict-patterns" + 
//				File.separator;
//		
//		File dir = new File(dirPath);
//		for(File f: dir.listFiles()) {
//			String filePath = f.getPath();
//			ConflictPattern cp = PatternParser.getConflictPattern(filePath);
//			System.out.println("########" + cp.getConflictName() + "##########");
//			System.out.println(cp.toString());
//		}
//		
//		String conflictPath = "src" + File.separator + "main" + File.separator + 
//				"resources" + File.separator + "conflict-patterns" + 
//				File.separator + "ParallelChanges.co";
//		
//		ConflictPattern cp = PatternParser.getConflictPattern(conflictPath);
//		System.out.println(cp.toString());
//	}
}
